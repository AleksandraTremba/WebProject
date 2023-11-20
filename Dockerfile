# Building the backend
FROM eclipse-temurin:17-jdk

WORKDIR /app
COPY target/*.jar /app.jar
COPY src/main/resources/application.properties .

EXPOSE 8090
CMD ["java","-jar","/app.jar"]
