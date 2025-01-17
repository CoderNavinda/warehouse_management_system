FROM openjdk:17

COPY target/*.jar app.jar

# Expose the application's port
EXPOSE 8082

ENTRYPOINT ["java","-jar","/app.jar"]