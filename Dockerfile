FROM gradle:9.8.1-jdk21-jammy AS builder

WORKDIR /app
COPY build.gradle.kts ./

RUN gradle dependencies --no-daemon

COPY src ./src

RUN gradle bootJar -x test --no-daemon

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

RUN mkdir -p tmp/ml_storage

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]