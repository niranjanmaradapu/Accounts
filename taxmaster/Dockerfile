FROM tomcat:8.0
USER root
WORKDIR /usr/local/tomcat/webapps/
RUN \
apt-get update && \
apt-get install unzip wget -y && \
rm -rf /var/lib/apt/lists/*
RUN wget -O /usr/local/tomcat/webapps/github-actions-artifact.zip https://pipelines.actions.githubusercontent.com/d6Cofa0qVMsIivjsBD5xwuNsr9LtXReiHHjolvFFcvzx40oKiT/_apis/pipelines/1/runs/7/signedartifactscontent?artifactName=github-actions-artifact&urlExpires=2021-08-16T14%3A52%3A27.6512940Z&urlSigningMethod=HMACV1&urlSignature=z5di4A1l%2F3iTyRomLXbBQysrvRzcAmzAjna9D4D9Feo%3D \
&& unzip '/usr/local/tomcat/webapps/github-actions-artifact.zip' -d /usr/local/tomcat/webapps/ && rm /usr/local/tomcat/webapps/github-actions-artifact.zip || true;
ENV JAVA_HOME=/usr/bin/java
WORKDIR /usr/local/tomcat/webapps
USER  root
EXPOSE 8080
CMD ['catalina.sh','run']
