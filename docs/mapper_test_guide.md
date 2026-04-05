# Mapperテスト ガイドライン

## 1. これは何のテスト？

Mapperの結合テストとは、**実際のDBに対してSQLを実行し、期待どおりの結果が返ってくるかを確認するテスト**です。

- Springの全機能は起動しない（軽い）
- 実DBに近いテスト用DBに対してSQLを実行する
- 「SQLを書いた人が自分で動作確認する」用途に最適

```
テストの重さイメージ:
単体テスト（モック）  ← 軽い
Mapperテスト         ← ちょうどいい ★ここ
@SpringBootTest      ← 重い
```

---

## 2. 使用するアノテーション

```java
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
```

| アノテーション | 役割 |
|---|---|
| `@MybatisTest` | MyBatis関連のBeanだけ起動。軽量。各テスト後にロールバックしてくれる |
| `@AutoConfigureTestDatabase(replace = NONE)` | インメモリDBではなく、実際のPostgreSQLを使う |

> **ポイント**: `@MybatisTest` はデフォルトでインメモリDB（H2）を使おうとします。
> PostgreSQL固有の構文（`SERIAL`、トリガー等）はH2では動かないので `replace = NONE` が必須です。

---

## 3. 依存関係の追加（pom.xml）

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter-test</artifactId>
    <version>3.0.3</version>
    <scope>test</scope>
</dependency>
```

---

## 4. テスト用設定ファイル

`src/test/resources/application.properties` を作成し、本番と同じDB設定を書きます。

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/nagahoribashi_walkdb
spring.datasource.username=nagahoribashi_walk
spring.datasource.password=nagahoribashi
spring.datasource.driver-class-name=org.postgresql.Driver

mybatis.configuration.map-underscore-to-camel-case=true
mybatis.mapper-locations=classpath:mapper/*.xml
```

> **注意**: テストは実際のDBに書き込みますが、`@MybatisTest` が各テスト後に自動でロールバックするので、データは残りません。

---

## 5. ファイルの置き場所

```
src/
├── main/
│   ├── java/com/example/nagahoribashi/
│   │   └── mapper/
│   │       └── SpotMapper.java
│   └── resources/
│       └── mapper/SpotMapper.xml
└── test/
    ├── java/com/example/nagahoribashi/
    │   └── mapper/
    │       └── SpotMapperTest.java   ← テストクラスはここ
    └── resources/
        └── application.properties   ← テスト用DB設定
```

---

## 6. サンプルコード：SpotMapperTest

```java
package com.example.nagahoribashi.mapper;

import com.example.nagahoribashi.dto.SpotSummary;
import com.example.nagahoribashi.entity.Spot;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SpotMapperTest {

    @Autowired
    SpotMapper spotMapper;

    // ------------------------------------------------
    // findAll
    // ------------------------------------------------

    @Test
    void findAll_スポット一覧が取得できること() {
        List<SpotSummary> result = spotMapper.findAll(0, 12);

        // 件数が1件以上あること
        assertThat(result).isNotEmpty();
        // 12件以下であること（ページネーション）
        assertThat(result).hasSizeLessThanOrEqualTo(12);
    }

    @Test
    void findAll_2ページ目が取得できること() {
        List<SpotSummary> page1 = spotMapper.findAll(0, 12);
        List<SpotSummary> page2 = spotMapper.findAll(12, 12);

        // 1ページ目と2ページ目でIDが重複しないこと
        List<Long> ids1 = page1.stream().map(SpotSummary::id).toList();
        List<Long> ids2 = page2.stream().map(SpotSummary::id).toList();
        assertThat(ids1).doesNotContainAnyElementsOf(ids2);
    }

    // ------------------------------------------------
    // findById
    // ------------------------------------------------

    @Test
    void findById_存在するIDで詳細が取得できること() {
        // data.sqlで投入済みのIDを指定
        Optional<SpotDetail> result = spotMapper.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().spotName()).isNotBlank();
    }

    @Test
    void findById_存在しないIDでemptyが返ること() {
        Optional<SpotDetail> result = spotMapper.findById(99999L);

        assertThat(result).isEmpty();
    }

    // ------------------------------------------------
    // findByCategoryId
    // ------------------------------------------------

    @Test
    void findByCategoryId_カテゴリIDで絞り込めること() {
        // カテゴリID=1（グルメ等）が存在する前提
        List<SpotSummary> result = spotMapper.findByCategoryId(1L, 0, 12);

        assertThat(result).isNotEmpty();
        // 全件、categoryNameが一致していること
        result.forEach(s -> assertThat(s.categoryName()).isNotBlank());
    }

    // ------------------------------------------------
    // searchByKeywords
    // ------------------------------------------------

    @Test
    void searchByKeywords_キーワードで検索できること() {
        // data.sqlにキーワード「ラーメン」を含むスポットが存在する前提
        List<SpotSummary> result = spotMapper.searchByKeywords("ラーメン");

        assertThat(result).isNotEmpty();
    }

    @Test
    void searchByKeywords_ヒットしないキーワードで空リストが返ること() {
        List<SpotSummary> result = spotMapper.searchByKeywords("xyzxyzxyz存在しないキーワード");

        assertThat(result).isEmpty();
    }

    // ------------------------------------------------
    // insert / softDelete
    // ------------------------------------------------

    @Test
    void insert_スポットを登録できること() {
        Spot spot = new Spot();
        spot.setSpotName("テスト用スポット");
        spot.setSubCategoryId(1L);

        spotMapper.insert(spot);

        // 登録後にIDが採番されていること
        assertThat(spot.getId()).isNotNull();
    }

    @Test
    void softDelete_論理削除後に一覧から除外されること() {
        // 登録して
        Spot spot = new Spot();
        spot.setSpotName("削除テスト用スポット");
        spot.setSubCategoryId(1L);
        spotMapper.insert(spot);
        Long id = spot.getId();

        // 削除して
        spotMapper.softDelete(id);

        // findAllで出てこないこと
        List<SpotSummary> allSpots = spotMapper.findAll(0, 100);
        List<Long> ids = allSpots.stream().map(SpotSummary::id).toList();
        assertThat(ids).doesNotContain(id);
    }
}
```

---

## 7. サンプルコード：FavoriteMapperTest

```java
package com.example.nagahoribashi.mapper;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FavoriteMapperTest {

    @Autowired
    FavoriteMapper favoriteMapper;

    // data.sqlで投入済みのIDを定数で持っておくと便利
    private static final Long TEST_USER_ID = 1L;
    private static final Long TEST_SPOT_ID = 1L;

    @Test
    void insert_お気に入り登録できること() {
        favoriteMapper.insert(TEST_USER_ID, TEST_SPOT_ID);

        boolean exists = favoriteMapper.existsByUserAndSpot(TEST_USER_ID, TEST_SPOT_ID);
        assertThat(exists).isTrue();
    }

    @Test
    void delete_お気に入り削除できること() {
        // 登録して
        favoriteMapper.insert(TEST_USER_ID, TEST_SPOT_ID);

        // 削除して
        favoriteMapper.delete(TEST_USER_ID, TEST_SPOT_ID);

        boolean exists = favoriteMapper.existsByUserAndSpot(TEST_USER_ID, TEST_SPOT_ID);
        assertThat(exists).isFalse();
    }

    @Test
    void existsByUserAndSpot_登録前はfalseを返すこと() {
        // data.sqlで登録されていない組み合わせを使う
        boolean exists = favoriteMapper.existsByUserAndSpot(99L, 99L);

        assertThat(exists).isFalse();
    }
}
```

---

## 8. AssertJのよく使うアサーション

```java
// 値が存在する（null/emptyでない）
assertThat(result).isNotNull();
assertThat(result).isNotEmpty();      // リスト
assertThat(result).isPresent();       // Optional

// 値が空・存在しない
assertThat(result).isEmpty();         // リスト or Optional
assertThat(result).isNull();

// 値の確認
assertThat(result).isEqualTo("期待値");
assertThat(result).isTrue();
assertThat(result).isGreaterThan(0);

// リストのサイズ
assertThat(list).hasSize(3);
assertThat(list).hasSizeLessThanOrEqualTo(12);

// リストの中身
assertThat(list).contains(element);
assertThat(list).doesNotContain(element);
```

---

## 9. テストメソッド名の付け方

メソッド名は **「何をしたとき・どうなるか」** が伝わるように日本語で書くと読みやすいです。

```java
// ✅ 良い例（状況と結果がわかる）
void findById_存在するIDで詳細が取得できること()
void findById_存在しないIDでemptyが返ること()
void softDelete_論理削除後に一覧から除外されること()

// ❌ 悪い例（何を確認しているか不明）
void testFindById()
void test1()
```

---

## 10. よくある失敗パターン

| 症状 | 原因 | 対処 |
|---|---|---|
| `H2` 関連のエラーが出る | `replace = NONE` を忘れた | `@AutoConfigureTestDatabase(replace = NONE)` を追加 |
| テスト後にDBにデータが残る | `@Transactional` が効いていない | `@MybatisTest` は自動でロールバックするので基本不要。ただし `@Commit` を付けると残るので注意 |
| `NullPointerException` on Mapper | `@Autowired` が効いていない | クラスに `@MybatisTest` が付いているか確認 |
| テストが遅い | `@SpringBootTest` を使っている | `@MybatisTest` に変更する |
| data.sqlのデータが入っていない | テスト用リソースの設定不備 | `src/test/resources/application.properties` のパスを確認 |
