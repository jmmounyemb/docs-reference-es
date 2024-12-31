# our base build image
FROM maven as maven

# copy the project files
COPY ./pom.xml ./pom.xml

# build all dependencies
RUN mvn dependency:go-offline -B

# copy your other files
COPY ./src ./src

# build for release
RUN mvn clean package

# our final base image
FROM openjdk:17-jdk-slim

# set deployment directory
WORKDIR /app

# copy over the built artifact from the maven image
COPY --from=maven target/docs-reference-es-0.0.1-SNAPSHOT.jar ./

# set the startup command to run your binary
CMD ["java", "-jar", "./docs-reference-es-0.0.1-SNAPSHOT.jar"]