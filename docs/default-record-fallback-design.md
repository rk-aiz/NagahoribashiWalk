# デフォルトレコード方式 設計方針

カテゴリ・サブカテゴリの削除時に、そのレコードを外部キー参照しているカラムを
あらかじめ用意した**デフォルトレコード**にフォールバックさせる方式を採用する。

---

## 1. スキーマ定義

### categories

```sql
CREATE TABLE categories (
  id            SERIAL PRIMARY KEY,
  name          VARCHAR(100) UNIQUE NOT NULL,
  display_order INTEGER NOT NULL,
  is_default    BOOLEAN DEFAULT FALSE  -- TRUE は「未分類」カテゴリ。全体で1件のみ
);
```

### sub_categories

```sql
CREATE TABLE sub_categories (
  id            SERIAL PRIMARY KEY,
  category_id   INTEGER NOT NULL,
  name          VARCHAR(100) NOT NULL,
  display_order INTEGER NOT NULL,
  is_default    BOOLEAN DEFAULT FALSE,  -- TRUE は「未分類」サブカテゴリ

  CONSTRAINT fk_sub_categories_category
    FOREIGN KEY (category_id) REFERENCES categories(id)
    ON DELETE RESTRICT,

  CONSTRAINT uq_sub_categories_category_name
    UNIQUE (category_id, name)
);
```

### spots（関連部分のみ）

```sql
sub_category_id INTEGER NOT NULL,  -- フォールバック設計

CONSTRAINT fk_spots_sub_category
  FOREIGN KEY (sub_category_id) REFERENCES sub_categories(id)
  ON DELETE RESTRICT
```

> **ポイント**: 外部キーはすべて `ON DELETE RESTRICT`。
> 削除処理はトリガー内で明示的に制御する。

---

## 2. デフォルトの一意性制約

各テーブルで `is_default = TRUE` が1件だけであることをDBレベルで保証する。

```sql
-- カテゴリ全体でデフォルトは1件
CREATE UNIQUE INDEX uq_categories_default
  ON categories (is_default) WHERE is_default = TRUE;

-- カテゴリごとにデフォルトサブカテゴリは1件
CREATE UNIQUE INDEX uq_sub_categories_default
  ON sub_categories (category_id, is_default) WHERE is_default = TRUE;
```

---

## 3. 初期データの自動生成

### カテゴリ作成時にデフォルトサブカテゴリを自動生成

カテゴリに行が INSERT されると、トリガーが `is_default = TRUE` の「未分類」サブカテゴリを自動生成する。
これにより、カテゴリを作るだけで spots のフォールバック先が必ず存在する。

```sql
CREATE OR REPLACE FUNCTION add_default_sub_category()
RETURNS TRIGGER AS '
    BEGIN
    INSERT INTO sub_categories (category_id, name, display_order, is_default)
    VALUES (NEW.id, ''未分類'', 99999, TRUE);
    RETURN NEW;
    END;
' LANGUAGE 'plpgsql';

CREATE TRIGGER trigger_add_default_sub_category
AFTER INSERT ON categories
FOR EACH ROW
EXECUTE FUNCTION add_default_sub_category();
```

### シードデータ

最低限、デフォルトカテゴリを1件投入する。サブカテゴリは上記トリガーが自動生成する。

```sql
INSERT INTO categories (name, display_order, is_default)
VALUES ('未分類', 999, TRUE);
-- → トリガーにより sub_categories ('未分類', 999, TRUE) が自動 INSERT される
```

---

## 4. トリガー設計

### 設計方針: WHEN 句による振り分け

同一テーブルの BEFORE DELETE トリガーを `WHEN` 句で `is_default` の TRUE / FALSE に振り分ける。
これにより、アルファベット順の実行制御が不要になり、
各トリガーが互いに干渉しないことが明確になる。

### 4-1. デフォルトレコード削除防止

フォールバック先が消えるとシステムが破綻するため、DBレベルでガードする。

```sql
CREATE OR REPLACE FUNCTION prevent_default_category_delete()
RETURNS TRIGGER AS '
BEGIN
    IF OLD.is_default = TRUE THEN
        RAISE EXCEPTION ''デフォルトカテゴリは削除できません'';
    END IF;
    RETURN OLD;
END;
' LANGUAGE 'plpgsql';

-- is_default = TRUE のときだけ発火
CREATE TRIGGER trg_prevent_default_delete
  BEFORE DELETE ON categories
  FOR EACH ROW
  WHEN (OLD.is_default = TRUE)
  EXECUTE FUNCTION prevent_default_category_delete();
```

```sql
CREATE OR REPLACE FUNCTION prevent_default_sub_category_delete()
RETURNS TRIGGER AS '
BEGIN
    IF OLD.is_default = TRUE THEN
        RAISE EXCEPTION ''デフォルトサブカテゴリは削除できません'';
    END IF;
    RETURN OLD;
END;
' LANGUAGE 'plpgsql';

-- is_default = TRUE のときだけ発火
CREATE TRIGGER trg_prevent_default_sub_category_delete
  BEFORE DELETE ON sub_categories
  FOR EACH ROW
  WHEN (OLD.is_default = TRUE)
  EXECUTE FUNCTION prevent_default_sub_category_delete();
```

### 4-2. サブカテゴリ削除時のフォールバック

削除されるサブカテゴリを参照している spots を、同一カテゴリ内のデフォルトサブカテゴリへ退避する。

```sql
CREATE OR REPLACE FUNCTION fallback_to_default_sub_category()
RETURNS TRIGGER AS '
    BEGIN
    UPDATE spots
    SET sub_category_id = (
        SELECT id FROM sub_categories
        WHERE category_id = OLD.category_id AND is_default = TRUE
    )
    WHERE sub_category_id = OLD.id;
    RETURN OLD;
    END;
' LANGUAGE 'plpgsql';

-- is_default = FALSE のときだけ発火（デフォルト自身は 4-1 で保護）
CREATE TRIGGER trigger_fallback_sub_category
  BEFORE DELETE ON sub_categories
  FOR EACH ROW
  WHEN (OLD.is_default = FALSE)
  EXECUTE FUNCTION fallback_to_default_sub_category();
```

### 4-3. カテゴリ削除時のフォールバック

カテゴリが削除されるとき、配下のサブカテゴリを削除せず**デフォルトカテゴリへ移動**する。
spots には一切触れないため処理が軽量。

名前の衝突（UNIQUE制約 `(category_id, name)`）が起きる場合はサフィックスを付与する。

```sql
CREATE OR REPLACE FUNCTION fallback_on_category_delete()
RETURNS TRIGGER AS '
DECLARE
    v_default_category_id INTEGER;
BEGIN
    SELECT id INTO v_default_category_id
    FROM categories
    WHERE is_default = TRUE;

    -- 名前が衝突するものはサフィックスを付与
    UPDATE sub_categories
    SET name = name || ''(OLD_'' || OLD.name || '')''
    WHERE category_id = OLD.id
    AND name IN (
        SELECT name FROM sub_categories
        WHERE category_id = v_default_category_id
    );

    -- 全件をデフォルトカテゴリに移動
    UPDATE sub_categories
    SET category_id = v_default_category_id
    WHERE category_id = OLD.id;

    RETURN OLD;
    END;
' LANGUAGE 'plpgsql';

-- is_default = FALSE のときだけ発火（デフォルト自身は 4-1 で保護）
CREATE TRIGGER trg_category_delete
  BEFORE DELETE ON categories
  FOR EACH ROW
  WHEN (OLD.is_default = FALSE)
  EXECUTE FUNCTION fallback_on_category_delete();
```

---

## 5. トリガー一覧

| トリガー名                                | 対象テーブル   | タイミング    | WHEN 条件            | 役割                                   |
| ----------------------------------------- | -------------- | ------------- | -------------------- | -------------------------------------- |
| `trigger_add_default_sub_category`        | categories     | AFTER INSERT  | —                    | デフォルトサブカテゴリ自動生成         |
| `trg_prevent_default_delete`              | categories     | BEFORE DELETE | `is_default = TRUE`  | デフォルトカテゴリ削除防止             |
| `trg_category_delete`                     | categories     | BEFORE DELETE | `is_default = FALSE` | サブカテゴリをデフォルトカテゴリへ移動 |
| `trg_prevent_default_sub_category_delete` | sub_categories | BEFORE DELETE | `is_default = TRUE`  | デフォルトサブカテゴリ削除防止         |
| `trigger_fallback_sub_category`           | sub_categories | BEFORE DELETE | `is_default = FALSE` | spots をデフォルトサブカテゴリへ退避   |
| `update_users_updated_at`                 | users          | BEFORE UPDATE | —                    | updated_at 自動更新                    |
| `update_spots_updated_at`                 | spots          | BEFORE UPDATE | —                    | updated_at 自動更新                    |
| `update_reviews_updated_at`               | reviews        | BEFORE UPDATE | —                    | updated_at 自動更新                    |

---

## 6. フォールバックの流れ

### サブカテゴリ「ラーメン」を削除した場合

```
spots（ラーメン屋A）→ sub_categories（ラーメン）  [削除対象]
                ↓ トリガーで付け替え
spots（ラーメン屋A）→ sub_categories（未分類）     [同カテゴリ内のデフォルト]
```

### カテゴリ「グルメ」を削除した場合

```
categories（グルメ）          [削除対象]
  └ sub_categories（ラーメン）
  └ sub_categories（カフェ）
  └ sub_categories（未分類）

    ↓ トリガーでデフォルトカテゴリへ移動

categories（未分類）
  └ sub_categories（ラーメン）
  └ sub_categories（カフェ）
  └ sub_categories（未分類(OLD_グルメ)）  ← 名前衝突のためサフィックス付与
  └ sub_categories（未分類）              ← 元からあったデフォルト
```

spots は一切変更されない。サブカテゴリの所属先が変わるだけ。

---

## 7. アプリケーション層での注意事項

- **カテゴリ作成時**: サブカテゴリの手動作成は不要。トリガーが「未分類」を自動生成する。
- **UI での削除制限**: デフォルトカテゴリ・サブカテゴリは削除ボタンを非表示にするか無効化する。トリガーは最終防衛線であり、UI で先にガードする。
- **JOIN**: `sub_category_id` が `NOT NULL` に移行完了すれば、`INNER JOIN` で統一できる。`LEFT JOIN` や `COALESCE` は不要になる。
- **`spots.sub_category_id` の NOT NULL 化**: 既存データの NULL を埋めた後、`ALTER TABLE` で `NOT NULL` 制約を追加する。

---

## 8. 設計判断の根拠

| 観点                   | NULL方式                           | デフォルトレコード方式（採用） |
| ---------------------- | ---------------------------------- | ------------------------------ |
| クエリの単純さ         | LEFT JOIN / COALESCE が必要        | INNER JOIN で統一              |
| アプリの考慮漏れリスク | NULLチェック漏れが各所で発生しうる | 外部キーが常に有効で安全       |
| DB側の整合性保証       | NULLは整合だが意味が曖昧           | レコードが存在し意味が明確     |
| 初期セットアップ       | 不要                               | シードデータ + トリガーが必要  |
| 経緯の追跡             | NULLで判別可能                     | 別途フラグやログが必要         |

---

## 9. TODO

- [ ] `spots.sub_category_id` の既存 NULL データを埋めて `NOT NULL` 制約を追加する
- [ ] フォールバック経緯の記録が必要な場合は `spots.is_fallback` フラグまたはログテーブルを検討する
