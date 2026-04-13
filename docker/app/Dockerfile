# ビルドステージ
FROM gradle:jdk21 AS build
WORKDIR /app
COPY . .
# テストをスキップしてビルドを実行
RUN gradle bootJar -x test --no-daemon

# 実行ステージ
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

# アップロード画像の保存先ディレクトリを作成
RUN mkdir -p /app/uploads/images

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
