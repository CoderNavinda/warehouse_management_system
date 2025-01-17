FROM openjdk:17

COPY target/*.jar app.jar

# Expose the application's port
EXPOSE 8085

ENTRYPOINT ["java","-jar","/app.jar"]