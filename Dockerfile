# 1. Folosim o imagine de Maven pentru a compila codul
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Compilăm și sărim peste teste pentru viteză
RUN mvn clean package -DskipTests

# 2. Folosim o imagine ușoară de Java pentru a rula aplicația
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
# Copiem jar-ul generat la pasul anterior
COPY --from=build /app/target/*.jar app.jar
# Comanda de start
ENTRYPOINT ["java", "-jar", "app.jar"]