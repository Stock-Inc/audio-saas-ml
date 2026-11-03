FROM gradle:9.8.1-jdk21-jammy AS builder

WORKDIR /app

COPY build.gradle.kts ./

COPY src ./src

RUN gradle clean bootJar -x test

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]