FROM maven:3.9.3-eclipse-temurin-17-focal AS build
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
COPY cert/keystore.p12 ./cert/keystore.p12
RUN mvn clean package

FROM openjdk:17-alpine
RUN apk update && apk upgrade --available && sync
WORKDIR /app
COPY --from=build /workspace/target/summary-tg-bot-*.jar ./app.jar
COPY --from=build /workspace/cert/keystore.p12 ./keystore.p12
CMD java -jar app.jar
