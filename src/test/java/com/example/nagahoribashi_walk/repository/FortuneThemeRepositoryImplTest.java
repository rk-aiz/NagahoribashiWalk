package com.example.nagahoribashi_walk.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.nagahoribashi_walk.entity.FortuneTheme;
import com.example.nagahoribashi_walk.repository.impl.FortuneThemeRepositoryImpl;

/**
 * FortuneThemeRepositoryImpl のユニットテスト
 *
 * ■ テストクラスの命名
 * テスト対象クラス名 + "Test" をつけるのが慣例
 * 例: FortuneThemeRepositoryImpl → FortuneThemeRepositoryImplTest
 *
 * ■ @BeforeEach
 * 各テストメソッドの前に実行されるセットアップ処理
 * テスト間でデータが干渉しないよう、毎回新しいインスタンスを生成する
 *
 * ■ テストメソッドの命名（日本語もOK）
 * 「何をテストするか」が一目でわかる名前をつける
 * 例: findById_存在するIDを指定した場合_テーマが返る
 *
 * ■ AssertJ（assertThat）
 * Spring Boot のテストライブラリに含まれるアサーションライブラリ
 * メソッドチェーンで直感的に検証できる
 * 例: assertThat(result).isNotNull().isEqualTo("期待値");
 */
class FortuneThemeRepositoryImplTest {

    // テスト対象のインスタンス
    private FortuneThemeRepositoryImpl repository;

    /**
     * 各テストメソッドの前に実行される
     * 毎回新しいインスタンスを作ることで、テスト間のデータ干渉を防ぐ
     */
    @BeforeEach
    void setUp() {
        repository = new FortuneThemeRepositoryImpl();
    }

    // =========================================================
    // findAll
    // =========================================================

    @Test
    void findAll_初期データが取得できる() {
        List<FortuneTheme> result = repository.findAll();

        // コンストラクタで insert() している
        assertThat(result).isNotEmpty();
    }

    // =========================================================
    // findById
    // =========================================================

    @Test
    void findById_存在するIDを指定した場合_テーマが返る() {
        Optional<FortuneTheme> result = repository.findById(1L);

        // Optional の中身が存在するかチェック
        assertThat(result).isPresent();
        assertThat(result.get().getMood()).isEqualTo("のんびり癒されたい");
    }

    @Test
    void findById_存在しないIDを指定した場合_空のOptionalが返る() {
        Optional<FortuneTheme> result = repository.findById(9999L);

        // 存在しない場合は empty
        assertThat(result).isEmpty();
    }

    // =========================================================
    // insert
    // =========================================================

    @Test
    void insert_新しいテーマを追加すると件数が増える() {
        int beforeSize = repository.findAll().size();

        repository.insert("テスト気分", "テスト,サンプル");

        assertThat(repository.findAll()).hasSize(beforeSize + 1);
    }

    @Test
    void insert_追加したテーマがfindByIdで取得できる() {
        repository.insert("テスト気分", "テスト,サンプル");

        // 初期20件の次は ID=21 になる
        Optional<FortuneTheme> result = repository.findById(21L);

        assertThat(result).isPresent();
        assertThat(result.get().getMood()).isEqualTo("テスト気分");
        assertThat(result.get().getKeywords()).isEqualTo("テスト,サンプル");
    }

    // =========================================================
    // save
    // =========================================================

    @Test
    void save_IDなしで保存すると新規追加される() {
        FortuneTheme theme = new FortuneTheme();
        theme.setMood("新規気分");
        theme.setKeywords("新規,キーワード");

        repository.save(theme);

        // save後にIDが自動採番される
        assertThat(theme.getId()).isNotNull();
        assertThat(repository.findAll()).hasSize(21);
    }

    @Test
    void save_既存IDで保存すると内容が上書きされる() {
        FortuneTheme theme = new FortuneTheme();
        theme.setId(1L);
        theme.setMood("上書き後の気分");
        theme.setKeywords("上書き,キーワード");

        repository.save(theme);

        Optional<FortuneTheme> result = repository.findById(1L);
        assertThat(result).isPresent();
        assertThat(result.get().getMood()).isEqualTo("上書き後の気分");
        // 件数は変わらない
        assertThat(repository.findAll()).hasSize(20);
    }

    // =========================================================
    // findRandom
    // =========================================================

    @Test
    void findRandom_指定件数だけ返る() {
        List<FortuneTheme> result = repository.findRandom(3, new Random());

        assertThat(result).hasSize(3);
    }

    @Test
    void findRandom_同じシードなら同じ結果が返る() {
        // Random にシード値を固定すると、毎回同じ乱数列が生成される
        // → 乱数を使うロジックを「再現性のある」テストにできる
        long seed = 42L;
        List<FortuneTheme> result1 = repository.findRandom(5, new Random(seed));
        List<FortuneTheme> result2 = repository.findRandom(5, new Random(seed));

        assertThat(result1)
                .extracting(FortuneTheme::getId)
                .isEqualTo(result2.stream().map(FortuneTheme::getId).toList());
    }

    @Test
    void findRandom_全件数を超える件数を指定しても例外が発生しない() {
        // 20件しかないのに100件要求 → 最大20件返る
        List<FortuneTheme> result = repository.findRandom(100, new Random());

        assertThat(result).hasSize(20);
    }
}
