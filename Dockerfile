# Výber základného obrazu
FROM openjdk:11-jdk as build

# Nastavenie pracovného adresára v kontajneri
WORKDIR /app

# Kopírovanie súborov Maven konfigurácie
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Načítanie závislostí
RUN ./mvnw dependency:go-offline

# Kopírovanie zdrojových súborov
COPY src src

# Zostavenie aplikácie
RUN ./mvnw package -DskipTests

# Výber základného obrazu pre finálnu fázu
FROM openjdk:11-jre-slim

# Nastavenie pracovného adresára v kontajneri
WORKDIR /app

# Kopírovanie súboru JAR z build fázy
COPY --from=build /app/target/*.jar app.jar

# Spustenie aplikácie
ENTRYPOINT ["java","-jar","app.jar"]