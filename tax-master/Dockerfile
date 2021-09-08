FROM openjdk:8
COPY  target/  target/
RUN  mv target/*.jar   target/tax-master.jar
EXPOSE 9191
ENTRYPOINT ["java", "-jar", "target/tax-master.jar"]
