# ---------------------------------------------------------
# ETAPA 1: Build (Compilare)
# Folosim o imagine de Maven cu Java 21
# ---------------------------------------------------------
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copiem doar pom.xml si il descarcam o data
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiem restul codului sursă
COPY src ./src

# CREAREA UNUI SETTINGS.XML TEMPORAR PENTRU A EVITA REPOSITORY-UL SNAPSHOT LIPSĂ
# Ștergem din pom.xml referințele la spring-snapshots, dar le punem aici (doar in caz ca era vreo referinta pe care nu o putem sterge)
# (In cazul tau, le-am pastrat pe cele din pom.xml, dar Railway ar trebui sa le ignore).

# Fortam compilarea
RUN mvn clean package -DskipTests -B

# ---------------------------------------------------------
# ETAPA 2: Run (Rulare)
# Folosim o imagine ușoară de Java 21
# ---------------------------------------------------------
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copiem JAR-ul final
COPY --from=build /app/target/hunt-0.0.1-SNAPSHOT.jar app.jar

# Expunem portul
EXPOSE 8080

# Comanda de start
ENTRYPOINT ["java", "-jar", "app.jar"]