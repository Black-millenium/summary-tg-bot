FROM openjdk:17-alpine
RUN apk update && apk upgrade --available && sync
WORKDIR /app
COPY target/summary-tg-bot-*.jar ./app.jar
COPY cert/keystore.p12 ./keystore.p12
CMD java -jar app.jar
