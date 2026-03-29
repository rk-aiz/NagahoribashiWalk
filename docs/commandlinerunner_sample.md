# CommandLineRunner を使った動作確認サンプル

JUnit を使わずに、アプリ起動時にサービス層の動作を手軽に確認する方法。

## 方法

`NagahoribashiWalkApplication` に `CommandLineRunner` を実装し、`run()` メソッドに確認コードを書く。
アプリ起動時に自動で実行される。

> **注意**: 確認が終わったら必ず削除すること。本番起動時にも実行されるため。

## サンプルコード

```java
@SpringBootApplication
public class NagahoribashiWalkApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(NagahoribashiWalkApplication.class, args);
    }

    // DI
    @Autowired
    UserService userService;

    // アプリ起動時に実行される
    @Override
    public void run(String... args) throws Exception {
        System.out.println("長堀橋さんぽアプリ起動");

        System.out.println("【テスト】ユーザー一覧をページング付きで取得:");

        Pageable pageable = Pageable.ofSize(12);
        userService.getWithPaging(pageable).get().forEach((user) -> { System.out.println(user); });
    }
}
```

## 実行結果の確認

コンソール（またはIDEのログ）に出力される。

```
長堀橋さんぽアプリ起動
【テスト】ユーザー一覧をページング付きで取得:
User(id=1, username=admin, ...)
User(id=2, username=user1, ...)
...
```
