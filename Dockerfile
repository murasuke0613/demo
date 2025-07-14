# ---------- Build Stage ----------
FROM maven:3.9.6-eclipse-temurin-17 AS build

# 作業ディレクトリ作成
WORKDIR /app

# ソースをコンテナ内にコピー
COPY . .

# Mavenでjarビルド（テストはスキップ）
RUN mvn clean package -DskipTests

# ---------- Run Stage ----------
FROM eclipse-temurin:17-jdk-alpine

# 作業ディレクトリ
WORKDIR /app

# jarファイルだけコピー
COPY --from=build /app/target/*.jar app.jar

# ポート解放
EXPOSE 8080

# Spring Bootアプリ起動
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
