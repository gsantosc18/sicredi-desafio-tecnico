FROM adoptopenjdk/openjdk11:latest

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src


EXPOSE 80

ENTRYPOINT ["./gradlew","clean","bootRun"]