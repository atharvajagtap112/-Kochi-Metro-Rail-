# Stage 1: Build the application using Maven and JDK 21
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Set working directory inside the container
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the entire project and build the JAR
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Create a lightweight image for running the app
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Expose application port
EXPOSE 5454

# Copy the built JAR from the build stage
COPY --from=build /app/target/KmrlInductionPlanningApplication-0.0.1-SNAPSHOT.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]