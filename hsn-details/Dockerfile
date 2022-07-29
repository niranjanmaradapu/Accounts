FROM openjdk:8
COPY  target/  target/
RUN  mv target/*.jar   target/hsn-details.jar

#RUN apt install curl unzip -y 
#RUN curl -O https://download.newrelic.com/newrelic/java-agent/newrelic-agent/current/newrelic-java.zip
#RUN unzip newrelic-java.zip  
#RUN mv newrelic  target/newrelic
#COPY newrelic.yml  target/newrelic/newrelic.yml

#ENV NEW_RELIC_APP_NAME="hsn-details"
#ENV NEW_RELIC_LICENSE_KEY="074cba2e27e9e88412445e3c424c6890FFFFNRAL"
#ENV NEW_RELIC_LOG_FILE_NAME="STDOUT"

# #clean up 
#RUN apt remove unzip curl -y
#RUN rm -rf newrelic-java.zip 
EXPOSE 8083
#ENTRYPOINT ["java","-javaagent:target/newrelic/newrelic.jar", "-Dspring.profiles.active=cloud", "-jar", "target/hsn-details.jar"]
ENTRYPOINT ["java", "-Dspring.profiles.active=cloud", "-jar", "target/hsn-details.jar"]


