FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace

COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

COPY src ./src
RUN mvn -q -DskipTests package dependency:copy-dependencies

FROM eclipse-temurin:17-jre
WORKDIR /app

RUN apt-get update \
    && apt-get install -y --no-install-recommends \
        libx11-6 \
        libxext6 \
        libxi6 \
        libxrender1 \
        libxtst6 \
        libfreetype6 \
        fontconfig \
    && rm -rf /var/lib/apt/lists/*

COPY --from=build /workspace/target/psychology-experiments-app-1.0-SNAPSHOT.jar /app/app.jar
COPY --from=build /workspace/target/dependency /app/lib
COPY src/main/resources/users.txt /app/data/users.txt

ENV USERS_FILE=/app/data/users.txt

CMD ["java", "-cp", "/app/app.jar:/app/lib/*", "lab.App"]
