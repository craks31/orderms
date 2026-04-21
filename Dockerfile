# ==========================================
# STAGE 1: The Builder (Compiles the Code)
# ==========================================
FROM maven:3.9-eclipse-temurin-17-alpine AS builder
WORKDIR /build

# 1. Copy POM first to cache dependencies (saves 3-5 minutes on future builds)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 2. Copy source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# ==========================================
# STAGE 2: The Runtime (Runs the Code Securely)
# ==========================================
# Using Chainguard for a zero-CVE, shell-less runtime environment
FROM cgr.dev/chainguard/jre:latest
WORKDIR /app

# 3. Extract the finished jar from the Builder stage
COPY --from=builder /build/target/*.jar app.jar

# 4. Expose the port your Spring Boot app is running on (8081 from your logs)
EXPOSE 8080

# 5. Start the application with container-aware memory limits
ENTRYPOINT ["java", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:+UseContainerSupport", \
    "-jar", "app.jar"]