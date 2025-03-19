# Use OpenJDK 21 image
FROM openjdk:21-jdk-slim
WORKDIR /app

# Copy the JAR file into the container
COPY target/Config-Server-0.0.1-SNAPSHOT.jar config-server.jar

# Expose the port
EXPOSE 8080

# Run the application with the correct JAR name
ENTRYPOINT ["java", "-jar", "config-server.jar"]
