# Výber základného obrazu pre build
FROM openjdk:17-jdk as build

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
FROM openjdk:17-jre

# Nastavenie pracovného adresára v kontajneri
WORKDIR /app

# Kopírovanie súboru JAR z build fázy
COPY --from=build /app/target/*.jar app.jar

# Spustenie aplikácie
ENTRYPOINT ["java","-jar","app.jar"]