# 業務連絡アプリ

## 📖 概要
このアプリは、Spring Boot と Thymeleaf を使った社内業務連絡システムです。  
部署ごとに投稿・閲覧ができ、ユーザー認証、最新投稿表示、投稿内容修正・削除、投稿履歴表示機能を備えています。

---

## 🚀 主な機能
- ✅ ユーザー認証（ログイン/ログアウト）
- ✅ 部署ごとの投稿・最新投稿表示
- ✅ 投稿履歴の確認
- ✅ 投稿内容の編集・削除
- ✅ Bootstrap5 によるレスポンシブデザイン

---

## 🛠 使用技術
- **バックエンド**: Spring Boot 3.x
- **フロントエンド**: Thymeleaf, Bootstrap 5, Quill.js
- **データベース**: PostgreSQL
- **ビルドツール**: Maven
- **その他**: Spring Security

---

## 💻 環境構築

1️⃣ クローン
```bash
git clone https://github.com/murasuke0613/WritingBord.git
cd demo

2️⃣ データベース設定
src/main/resources/application.properties を編集

spring.datasource.url=jdbc:postgresql://dpg-d1nraq6r433s738j8a0g-a.oregon-postgres.render.com:5432/portfolio_11jc
spring.datasource.username=portfolio_11jc_user
spring.datasource.password=Hxg07wmS0uO7PVKtCiKNSmx02HDCFmzg
spring.datasource.hikari.data-source-properties.sslmode=require

spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

spring.jpa.show-sql=true

spring.jpa.properties.hibernate.format_sql=true

logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE

3️⃣ ビルド & 実行
./mvnw clean package
java -jar target/demo-0.0.1-SNAPSHOT.jar
