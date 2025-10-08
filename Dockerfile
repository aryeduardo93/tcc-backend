# Usa o JDK 17
FROM eclipse-temurin:17-jdk

# Define o diretório de trabalho
WORKDIR /app

# Copia tudo do projeto
COPY . .

# Dá permissão de execução ao gradlew
RUN chmod +x ./gradlew

# Roda o build
RUN ./gradlew build -x test

# Expõe a porta 8080
EXPOSE 8080

# Comando para rodar o .jar
CMD ["java", "-jar", "build/libs/*.jar"]
