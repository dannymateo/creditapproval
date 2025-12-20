# Stage 1: Construction
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Leverage layer caching for dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy code and build
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Execution (Runtime)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Install wget for Docker healthcheck
RUN apk add --no-cache wget

# Create a system user to avoid running as root
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy the generated JAR
COPY --from=build /app/target/*.jar app.jar

# Expose inner port
EXPOSE 8080

# Optimized memory configuration for containers
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]