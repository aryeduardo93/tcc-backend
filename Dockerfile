WORKDIR /app
COPY . .
RUN ./gradlew build -x test
CMD ["java", "-jar", "/app/build/libs/demo-0.0.1-SNAPSHOT.jar"]
