# コーディング規約

## プロジェクト概要

- **プロジェクト名**: 長堀橋周辺 観光サイト
- **技術スタック**: Java 21 / Spring Boot / MyBatis / Thymeleaf / PostgreSQL
- **IDE**: Eclipse
- **バージョン管理**: GitHub

---

## 1. パッケージ構成

```
com.example.nagahoribashi   ← ベースパッケージ （プロジェクトに合わせて変更）
├── controller/             ← コントローラ（画面遷移・リクエスト処理）
├── service/                ← ビジネスロジックインターフェース
        └── impl/           ← ビジネスロジック実装
├── repository/             ← MyBatis Mapper インターフェース
├── entity/                 ← DBテーブルに対応するエンティティ
├── dto/                    ← 画面にデータを表示するためのDTO
├── form/                   ← 画面の入力フォーム用クラス
├── config/                 ← 設定クラス（SecurityConfig 等）
└── util/                   ← ユーティリティクラス（共通処理）
```

### パッケージのルール

- Controller から直接 Mapper を呼ばない（必ず Service を経由する）
- 1つの Controller に機能を詰め込みすぎない（目安: 1ファイル200行以内）
- エンティティとフォームは分ける（DB構造と画面入力は別物として扱う）

---

## 2. 命名規則

### クラス名

| 種類                        | 命名パターン                                                        | 例                     |
| --------------------------- | ------------------------------------------------------------------- | ---------------------- |
| Controller                  | ○○Controller                                                        | SpotController         |
| Service（インターフェース） | ○○Service                                                           | SpotService            |
| Service（実装）             | ○○ServiceImpl                                                       | SpotServiceImpl        |
| Mapper                      | ○○Mapper                                                            | SpotMapper             |
| Entity                      | テーブル名に対応                                                    | Spot, User, Review     |
| DTO                         | 用途に応じて○○Summaryなど。エンティティと名前が衝突する場合は ○○DTO | SpotSummary、ReviewDTO |
| Form                        | ○○Form                                                              | ReviewForm, LoginForm  |
| Config                      | ○○Config                                                            | SecurityConfig         |

### メソッド名

| 処理内容     | プレフィックス        | 例                                                                                   |
| ------------ | --------------------- | ------------------------------------------------------------------------------------ |
| 一覧取得     | findAll / getAll      | findAll()                                                                            |
| 1件取得      | findById / getById    | findById(Long id)                                                                    |
| ページで取得 | getPage                | getPage(Pageable pageable) |
| 検索         | search / findBy○○     | searchByCategory(Long categoryId)                                                    |
| 登録         | create / insert / add | create(Review review)                                                                |
| 更新         | update                | update(Spot spot)                                                                    |
| 削除         | delete / remove       | delete(Long id)                                                                      |

### 変数名

- キャメルケース（lowerCamelCase）を使用: `spotName`, `createdAt`
- リストは複数形にする: `spots`, `reviews`
- 略語は避ける: `cnt` → `count`, `btn` → 不使用（Javaでは使わない）

### 定数

- すべて大文字のスネークケース: `MAX_REVIEW_LENGTH`, `DEFAULT_PAGE_SIZE`

---

## 3. Controllerの書き方

```java
@Controller
@RequestMapping("/spot")
@RequiredArgsConstructor
public class SpotController {

    private final SpotService spotService;

    // 一覧画面
    @GetMapping("/category/all")
    public String showAllSpots(Model model) {
        model.addAttribute("spots", spotService.findAll());
        return "spot/list";  // templates/spot/list.html
    }

    // 詳細画面
    @GetMapping("/{id}")
    public String showDetail(@PathVariable Long id, Model model) {
        model.addAttribute("spot", spotService.findById(id));
        return "spot/detail";
    }
}
```

### ルール

- `@Controller` を使用（REST APIではないので `@RestController` は使わない）
- URLは名詞・単数形を基本とする: `/spot`, `/review`
- 画面表示は `@GetMapping`、データ送信は `@PostMapping`
- PRG パターン: POST処理後は `return "redirect:/spot";` でリダイレクト

---

## 4. Service の書き方

```java
@Service
public class SpotServiceImpl implements SpotService {

    private final SpotMapper spotMapper;

    public SpotServiceImpl(SpotMapper spotMapper) {
        this.spotMapper = spotMapper;
    }

    @Override
    public List<Spot> findAll() {
        return spotMapper.findAll();
    }
}
```

### ルール

- サービス・リポジトリはインターフェース + 実装クラスの構成にする
- `@Transactional` をサービス実装クラスに付ける

---

## 5. MyBatis（Mapper）の書き方

### XML方式を基本とする

```
src/main/resources/mapper/SpotMapper.xml
```

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.example.nagahoribashi.mapper.SpotMapper">

    <select id="findAll" resultType="com.example.nagahoribashi.entity.Spot">
        SELECT id, name, category, address, description, image_url, created_at
        FROM spots
        ORDER BY id
    </select>

</mapper>
```

### ルール

- SQL は XML ファイルに書く
- SELECT文では `SELECT *` を使わず、カラムを明示する
- テーブル名・カラム名はスネークケース: `spots`, `created_at`
- Java側のフィールド名はキャメルケース: `createdAt`
    - `application.properties` に `mybatis.configuration.map-underscore-to-camel-case=true` を設定
- 1件取得の際はOptional<T>を使用し、サービスでチェック、コントローラーに想定外のNULLが渡らないようにする

---

## 6. Thymeleaf（テンプレート）の書き方

### ディレクトリ構成

```
src/main/resources/
├── templates/
│   ├── layout/
│   │   └── default.html      ← 共通レイアウト
│   ├── spot/
│   │   ├── list.html          ← スポット一覧
│   │   └── detail.html        ← スポット詳細
│   ├── user/
│   │   ├── login.html         ← ログイン
│   │   ├── register.html      ← 会員登録
│   │   └── register-complete.html      ← 会員登録完了
│   └── error/
│       └── 404.html
└── static/
    ├── css/
    │   └── style.css
    ├── js/
    │   └── main.js
    └── images/
```

### ルール

- 共通レイアウト（ヘッダー・フッター）は `layout/default.html` にまとめる
- CSS / JS は `static/` 配下に配置し、Thymeleaf の `@{/css/style.css}` で参照

---

## 7. データベース規約

### テーブル名・カラム名

- テーブル名: スネークケース・複数形（`spots`, `users`, `reviews`, `favorites`）
- カラム名: スネークケース（`spot_id`, `created_at`）
- 主キー: `id`（SERIAL / BIGSERIAL）
- 外部キー: `参照先テーブル名の単数形_id`（`spot_id`, `user_id`）
- 日時: `created_at`, `updated_at`

---

## 8. Git 運用ルール

### ブランチ戦略

```
master      ← 常に動く状態を維持
  ├── feature/spot-list       ← スポット一覧機能
  ├── feature/login           ← ログイン機能
  └── feature/review          ← レビュー機能
```

### ルール

- `masterに直接 push しない
- 機能ごとに `feature/○○` ブランチを切る
- 作業完了後、`master` へ Pull Request を出す
- マージ前に最低1人がレビューする

### コミットメッセージ

```
[種別] 変更内容の要約

例:
[add] スポット一覧画面を作成
[fix] 検索機能のバグを修正
[update] レビューフォームのバリデーション追加
[remove] 不要なコメントを削除
[docs] READMEを更新
```

---

## 9. コーディングスタイル

### 全般

- 文字コード: UTF-8
- 改行コード: LF（Eclipseの設定で統一する）
- 1行の文字数: 120文字以内を目安
- 未使用の import は削除する

### コメント

- クラスには JavaDoc コメントを付ける
- 処理の意図がわかりにくい箇所にコメントを書く
- 「何をしているか」ではなく「なぜそうしているか」を書く

```java
// 良い例
// ログインユーザーのみ表示するため認証チェック
if (loginUser == null) {
    return "redirect:/login";
}

// 悪い例
// nullチェック
if (loginUser == null) {
    return "redirect:/login";
}
```

### アクセス修飾子

- フィールドは `private` にする
- 不必要に `public` にしない

---

## 10. Eclipseの共通設定

チーム全員が以下の設定を揃えること:

- **文字コード**: UTF-8（ワークスペース全体）
- **改行コード**: LF
- **フォーマッター**: Eclipse標準（インデント: スペース4）
- **保存時アクション**: import の整理、未使用 import の削除
- **Lombok プラグイン**: インストール済みであること

---
