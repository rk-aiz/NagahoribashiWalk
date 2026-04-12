-- ============================================================
-- schema.sql  長堀橋さんぽ DDL
-- ============================================================
-- このファイルを実行するたびに全テーブルを DROP → CREATE する。
-- 開発中のリセット用設定。本番環境では使用しないこと。
-- ============================================================


-- 外部キー制約があるため、参照先より先に参照元を DROP する
DROP TABLE IF EXISTS favorites;
DROP TABLE IF EXISTS reviews;
DROP TABLE IF EXISTS spot_photos;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS spots;
DROP TABLE IF EXISTS sub_categories;
DROP TABLE IF EXISTS categories;

-- ============================================================
-- テーブル定義
-- ============================================================

-- カテゴリ（グルメ／観光スポット／ショッピング／娯楽／カフェ＋未分類）
CREATE TABLE categories(
	id SERIAL PRIMARY KEY,
	name VARCHAR(100) UNIQUE NOT NULL,
	display_order INTEGER,
	is_default BOOLEAN DEFAULT FALSE  -- TRUE は「未分類」カテゴリ。全体で1件のみ（後述のINDEXで保証）
);

-- 「その他」カテゴリは全体でただ1件だけ存在できる
CREATE UNIQUE INDEX uq_categories_default
ON categories (is_default) WHERE is_default = TRUE;

-- サブカテゴリ（カテゴリの中分類）
CREATE TABLE sub_categories (

	id SERIAL PRIMARY KEY,

	category_id INTEGER NOT NULL,

	name VARCHAR(100) NOT NULL,

	-- カテゴリ内での表示順（1始まりの連番）
	-- 「その他」は add_default_sub_category トリガーが NULL を自動セットする
    display_order INTEGER,

	-- TRUE は「未分類」サブカテゴリ。カテゴリINSERT時にトリガーが自動生成する。
	-- DBレベルでカテゴリごとに1件のみ許可（後述のINDEXで保証）
	is_default BOOLEAN DEFAULT FALSE,

	-- サブカテゴリ削除時は DB トリガーが spots を「その他」へ自動フォールバックする
	CONSTRAINT fk_sub_categories_category
		FOREIGN KEY (category_id) REFERENCES categories(id)
		ON DELETE RESTRICT,

	-- 同一カテゴリ内でサブカテゴリ名の重複を禁止
	CONSTRAINT uq_sub_categories_category_name
		UNIQUE (category_id, name)
);

-- カテゴリごとに「その他」サブカテゴリは1件のみ
CREATE UNIQUE INDEX uq_sub_categories_default
ON sub_categories (category_id, is_default) WHERE is_default = TRUE;

-- スポット（観光地・飲食店の情報）
CREATE TABLE spots(
	id SERIAL PRIMARY KEY,
	spot_name VARCHAR(255) NOT NULL,
	sub_category_id INTEGER NOT NULL,
	website_url VARCHAR(255),
	gmap_url VARCHAR(500),
	address VARCHAR(255),
	business_hours TEXT,
	closed_days TEXT,
	estimated_budget VARCHAR(255),
	keywords VARCHAR(255),       -- カンマ区切りで複数キーワードを格納。検索に使用
	details TEXT,
	deleted_at TIMESTAMP,        -- NULL = 公開中。論理削除は日時をセット
	created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	pv_count INTEGER NOT NULL DEFAULT 0,
	-- サブカテゴリ削除時は DB トリガーが spots を「未分類」へ自動フォールバックする
	CONSTRAINT fk_spots_sub_category
		FOREIGN KEY (sub_category_id) REFERENCES sub_categories(id)
		ON DELETE RESTRICT
);

CREATE TABLE users (
	id SERIAL PRIMARY KEY,
	username VARCHAR(60) UNIQUE NOT NULL,
	password VARCHAR(255) NOT NULL,           -- BCrypt ハッシュ済みで保存
	email VARCHAR(255) UNIQUE NOT NULL,
	role VARCHAR(20) NOT NULL DEFAULT 'USER', -- 'USER' または 'ADMIN'
	display_name VARCHAR(50) NOT NULL,
	deleted_at TIMESTAMP,                     -- NULL = 有効。論理削除は日時をセット
	created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	enabled BOOLEAN DEFAULT TRUE,              -- 管理者による一時的な無効化フラグ（論理削除とは別）
	point NUMERIC NOT NULL DEFAULT 0,
    last_drawn_at TIMESTAMP,                                             -- 直近におみくじを引いた日時
    recommended_spot_id INTEGER REFERENCES spots(id) ON DELETE SET NULL -- おみくじでおすすめされたスポット
);

-- レビュー（5段階評価＋コメント）
CREATE TABLE reviews (
	id SERIAL PRIMARY KEY,
	user_id INTEGER NOT NULL,
	spot_id INTEGER NOT NULL,
	rating INTEGER NOT NULL CHECK (rating BETWEEN 1 AND 5),
	comment TEXT,
	created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT fk_reviews_user
		FOREIGN KEY (user_id) REFERENCES users(id)
		ON DELETE CASCADE,
	CONSTRAINT fk_reviews_spot
		FOREIGN KEY (spot_id) REFERENCES spots(id)
		ON DELETE CASCADE,
	-- 1ユーザーが同一スポットに複数レビューを投稿できないようにする
	CONSTRAINT uq_reviews_user_spot
		UNIQUE (user_id, spot_id)
);

-- お気に入り（ユーザーとスポットの中間テーブル）
CREATE TABLE favorites (
	id SERIAL PRIMARY KEY,
	user_id INTEGER NOT NULL,
	spot_id INTEGER NOT NULL,
	created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT fk_favorites_user
		FOREIGN KEY (user_id) REFERENCES users(id)
		ON DELETE CASCADE,
	CONSTRAINT fk_favorites_spot
		FOREIGN KEY (spot_id) REFERENCES spots(id)
		ON DELETE CASCADE,
	-- 1ユーザーが同一スポットを重複登録できないようにする
	CONSTRAINT uq_favorites_user_spot
		UNIQUE (user_id, spot_id)
);

-- スポット写真（1スポットに複数枚）
CREATE TABLE spot_photos (
	id SERIAL PRIMARY KEY,
	spot_id INTEGER NOT NULL,
	photo_url VARCHAR(255) NOT NULL,
	display_order INTEGER NOT NULL,      -- スポット内での表示順
	CONSTRAINT fk_spot_photos_spot
		FOREIGN KEY (spot_id) REFERENCES spots(id)
		ON DELETE CASCADE
);

-- ============================================================
-- トリガー関数
-- ============================================================

-- カテゴリ新規追加時に「その他」サブカテゴリを自動生成する。
-- これにより spots.sub_category_id は常に有効なレコードを参照できる
-- display_order=NULL で一覧の末尾に固定、is_default=TRUE で削除不可とする。
CREATE OR REPLACE FUNCTION add_default_sub_category()
RETURNS TRIGGER AS '
    BEGIN
    INSERT INTO sub_categories (category_id, name, display_order, is_default)
    VALUES (NEW.id, ''その他'', NULL, TRUE);
    RETURN NEW;
    END;
' LANGUAGE 'plpgsql';

-- spots テーブルのサブカテゴリが削除されたときのフォールバック
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

CREATE OR REPLACE FUNCTION fallback_on_category_delete()
RETURNS TRIGGER AS '
DECLARE
    v_default_category_id         INTEGER;
    v_default_sub_category_id     INTEGER;
    v_old_default_sub_category_id INTEGER;
BEGIN
    -- デフォルトカテゴリ（未分類）のIDを取得
    SELECT id INTO v_default_category_id
    FROM categories
    WHERE is_default = TRUE;

    -- 「未分類/その他」サブカテゴリのIDを取得
    SELECT id INTO v_default_sub_category_id
    FROM sub_categories
    WHERE category_id = v_default_category_id AND is_default = TRUE;

    -- 削除されるカテゴリの「その他」（is_default=TRUE）サブカテゴリのIDを取得
    SELECT id INTO v_old_default_sub_category_id
    FROM sub_categories
    WHERE category_id = OLD.id AND is_default = TRUE;

    -- 削除カテゴリの「その他」に属する spots を「未分類/その他」へ移動
    UPDATE spots
    SET sub_category_id = v_default_sub_category_id
    WHERE sub_category_id = v_old_default_sub_category_id;

    -- prevent_default_sub_category_delete トリガーをくぐるため is_default を FALSE にしてから削除
    UPDATE sub_categories SET is_default = FALSE WHERE id = v_old_default_sub_category_id;
    DELETE FROM sub_categories WHERE id = v_old_default_sub_category_id;

    -- 残りの非デフォルトサブカテゴリで名前が衝突するものはサフィックスを付与
    UPDATE sub_categories
    SET name = name || ''(OLD_'' || OLD.name || ''_'' || TO_CHAR(NOW(), ''YYYYMMDD_HH24MISS'') || '')''
    WHERE category_id = OLD.id
    AND name IN (
        SELECT name FROM sub_categories
        WHERE category_id = v_default_category_id
    );

    -- 残りの非デフォルトサブカテゴリをデフォルトカテゴリへ移動
    UPDATE sub_categories
    SET category_id = v_default_category_id
    WHERE category_id = OLD.id;

    RETURN OLD;
END;
' LANGUAGE 'plpgsql';

CREATE OR REPLACE FUNCTION prevent_default_category_delete()
RETURNS TRIGGER AS '
BEGIN
    IF OLD.is_default = TRUE THEN
        RAISE EXCEPTION ''デフォルトカテゴリは削除できません'';
    END IF;
    RETURN OLD;
END;
' LANGUAGE 'plpgsql';

CREATE OR REPLACE FUNCTION prevent_default_sub_category_delete()
RETURNS TRIGGER AS '
BEGIN
    IF OLD.is_default = TRUE THEN
        RAISE EXCEPTION ''デフォルトサブカテゴリは削除できません'';
    END IF;
    RETURN OLD;
END;
' LANGUAGE 'plpgsql';


-- ============================================================
-- トリガー バインド
-- ============================================================

-- categories に行が追加されたとき「未分類」サブカテゴリを自動生成
CREATE TRIGGER trigger_add_default_sub_category
AFTER INSERT ON categories
FOR EACH ROW
EXECUTE FUNCTION add_default_sub_category();

-- 通常のカテゴリ削除時、参照している sub_categories を「未分類カテゴリ」へ自動フォールバック
-- WHEN で is_default = FALSE のみ対象とし、「未分類カテゴリ」自体には次の guard トリガーが対応する
CREATE TRIGGER trg_category_delete
BEFORE DELETE ON categories
FOR EACH ROW
WHEN (OLD.is_default = FALSE)
EXECUTE FUNCTION fallback_on_category_delete();

-- デフォルトカテゴリ自体の削除を防ぐ
-- WHEN で is_default = TRUE のみ発火し、通常カテゴリの削除には干渉しない
CREATE TRIGGER trg_prevent_default_delete
BEFORE DELETE ON categories
FOR EACH ROW
WHEN (OLD.is_default = TRUE)
EXECUTE FUNCTION prevent_default_category_delete();

-- 通常のサブカテゴリ削除時、参照している spots を同カテゴリの「未分類」へ自動フォールバック
-- WHEN で is_default = FALSE のみ対象とし、「未分類」自体には次の guard トリガーが対応する
CREATE TRIGGER trigger_fallback_sub_category
BEFORE DELETE ON sub_categories
FOR EACH ROW
WHEN (OLD.is_default = FALSE)
EXECUTE FUNCTION fallback_to_default_sub_category();

-- デフォルトサブカテゴリ自体の削除を防ぐ
-- WHEN で is_default = TRUE のみ発火し、通常サブカテゴリの削除には干渉しない
CREATE TRIGGER trg_prevent_default_sub_category_delete
BEFORE DELETE ON sub_categories
FOR EACH ROW
WHEN (OLD.is_default = TRUE)
EXECUTE FUNCTION prevent_default_sub_category_delete();