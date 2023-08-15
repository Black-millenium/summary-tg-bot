# Шаг 1: Предзагрузка Maven-зависимостей в отдельный слой, для возможности его сохранения в кеше
FROM maven:3.9.3-eclipse-temurin-17-alpine AS dependencies
WORKDIR /workspace
COPY pom.xml .
RUN mvn dependency:go-offline

# Шаг 2: Сборка приложения на основе слоя с зависимостями
FROM dependencies AS build
COPY src ./src
COPY cert/keystore.p12 ./cert/keystore.p12
RUN mvn clean package

# Финальный шаг: Создание образа для выполнения
FROM eclipse-temurin:17-jre-alpine
RUN apk update && apk upgrade --available && sync
WORKDIR /app
COPY --from=build /workspace/target/summary-tg-bot-*.jar ./app.jar
COPY --from=build /workspace/cert/keystore.p12 ./keystore.p12
CMD java -jar app.jar
