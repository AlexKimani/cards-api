# Add maven as part of the docker image to build project
FROM maven:3.9.3-amazoncorretto-20 AS build

# Add Log folder for logs and grant permissions
RUN mkdir -p /var/log/
RUN chmod a+rwx -R /var/log/

# Copy the code to build
COPY src /usr/app/src
COPY pom.xml /usr/app

# Build the code and cache the dependency jars
RUN mvn dependency:go-offline -B

RUN mvn clean package -DskipTests

# Create the run target service
FROM openjdk:20-slim

MAINTAINER joealexkimani

# Copy build jar into run folder
COPY --from=build /usr/app/target/*.jar /usr/app/cards-api.jar

# Start the jar file
ENTRYPOINT ["java","-jar","/usr/app/cards-api.jar"]