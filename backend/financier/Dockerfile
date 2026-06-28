FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B -q clean package -DskipTests -Dtypescript.generator.skip=true

FROM eclipse-temurin:21-jre
WORKDIR /app
RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8081
HEALTHCHECK --interval=10s --timeout=5s --start-period=60s --retries=10 \
    CMD curl -fsS http://localhost:8081/api/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
