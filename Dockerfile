# Stage 1: Construction
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Aprovechar el caché para las dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar código y compilar
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Execution (Runtime)
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Cambiamos 'apk' por 'apt-get' que es el de Ubuntu/Jammy
# Instalamos wget para el healthcheck
RUN apt-get update && apt-get install -y wget && rm -rf /var/lib/apt/lists/*

# Crear usuario de sistema (Ubuntu usa groupadd/useradd)
RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

# Copiar el JAR generado
COPY --from=build /app/target/*.jar app.jar

# Exponer puerto
EXPOSE 8080

# Configuración de memoria
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]