FROM openjdk:8
COPY  target/  target/
RUN  mv target/*.jar   target/payment-gateway.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "target/payment-gateway.jar"]
