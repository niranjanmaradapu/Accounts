FROM openjdk:8
COPY  target/  target/
RUN  mv target/*.jar   target/hsn-details.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "target/hsn-details.jar"]
