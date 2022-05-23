FROM openjdk:11.0.13-jre-slim

USER root

RUN apt-get update

RUN apt-get update && apt-get install -y gnupg2

RUN apt-get -y install apt-transport-https ca-certificates curl software-properties-common

RUN curl -fsSL https://download.docker.com/linux/ubuntu/gpg | apt-key add -

RUN apt-key fingerprint 0EBFCD88

RUN add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
xenial \
stable"

RUN apt-get update

RUN apt-get -y install docker-ce docker-ce-cli

WORKDIR /app

COPY target/*-runner.jar application.jar

ENTRYPOINT ["java", "-jar", "application.jar"] 
