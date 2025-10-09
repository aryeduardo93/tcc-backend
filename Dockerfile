# Etapa 1: Build da aplicação
FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY . .
RUN ./gradlew build -x test

# Etapa 2: Executar a aplicação
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/build/libs/demo-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]
