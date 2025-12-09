# ---------------------------------------------------------
# ETAPA 1: Build (Compilare)
# Folosim o imagine de Maven care are Java 21 instalat
# ---------------------------------------------------------
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copiem fișierele proiectului în container
COPY . .

# Compilăm proiectul și sărim peste teste (pentru viteză la deploy)
RUN mvn clean package -DskipTests

# ---------------------------------------------------------
# ETAPA 2: Run (Rulare)
# Folosim o imagine ușoară de Java 21 (Alpine Linux)
# ---------------------------------------------------------
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copiem jar-ul generat la pasul anterior
# Atentie: Spring Boot genereaza un singur .jar in target/
COPY --from=build /app/target/*.jar app.jar

# Expunem portul (informativ pentru Railway)
EXPOSE 8080

# Comanda de start
ENTRYPOINT ["java", "-jar", "app.jar"]