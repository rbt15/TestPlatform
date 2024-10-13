# Build stage
FROM gradle:8.10.2-jdk21-alpine AS build
WORKDIR /home/gradle/src
COPY --chown=gradle:gradle . .
RUN gradle build --no-daemon

# Agent stage
FROM us-docker.pkg.dev/grafanalabs-global/docker-grafana-opentelemetry-java-prod/grafana-opentelemetry-java:2.8.0 AS agent

# Run stage
FROM openjdk:21-jdk-slim

ENV OTEL_EXPORTER_OTLP_PROTOCOL="http/protobuf"
ENV OTEL_EXPORTER_OTLP_ENDPOINT="https://otlp-gateway-prod-eu-west-2.grafana.net/otlp"
ENV OTEL_EXPORTER_OTLP_HEADERS="Authorization=Basic MTA2MzA5MzpnbGNfZXlKdklqb2lNVEkwTlRneU9TSXNJbTRpT2lKemRHRmpheTB4TURZek1Ea3pMVzkwYkhBdGQzSnBkR1V0ZEdWemRDSXNJbXNpT2lKbU1YTlJTams0T1dkak4ycFpOalpIYW5jNE0wTkxiamNpTENKdElqcDdJbklpT2lKd2NtOWtMV1YxTFhkbGMzUXRNaUo5ZlE9PQ=="
ENV OTEL_SERVICE_NAME="test-platform"

WORKDIR /app
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar
COPY --from=agent --chown=cnb /javaagent.jar /app/javaagent.jar
EXPOSE 8080

ENV JAVA_TOOL_OPTIONS=-javaagent:/app/javaagent.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]