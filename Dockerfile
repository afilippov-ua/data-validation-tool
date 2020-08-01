FROM openjdk:13-alpine
EXPOSE 8092
COPY build/libs/data-validation-tool-0.0.1-SNAPSHOT.jar data-validation-tool-0.0.1-SNAPSHOT.jar
CMD exec java -jar data-validation-tool-0.0.1-SNAPSHOT.jar --spring.config.location=classpath:/stage.properties