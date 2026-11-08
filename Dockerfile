FROM gradle:9.8.1-jdk21-jammy AS builder

WORKDIR /app
COPY build.gradle.kts settings.gradle.kts ./

RUN gradle dependencies --no-daemon

COPY src ./src

RUN gradle bootJar -x test --no-daemon

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

ENV JAVA_TOOL_OPTIONS="-Dorg.apache.avro.SERIALIZABLE_PACKAGES=com.example.ml.infrastructure.kafka.dto"

RUN mkdir -p tmp/ml_storage

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "app.jar"]