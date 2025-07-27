# BUILDER
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /workspace

COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN ./mvnw dependency:go-offline

COPY src ./src
RUN ./mvnw clean package -DskipTests

# RUNNER
FROM eclipse-temurin:21-jre-alpine

COPY --from=builder /workspace/target/*.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]