# inspired by https://community.jenkins.io/t/how-to-generate-a-docker-with-the-desired-plugins-installed/3453
#RUN jenkins-plugin-cli --plugins "blueocean:1.25.6 docker-workflow:1.29"

# maven plugin: https://plugins.jenkins.io/maven-plugin/#releases

FROM jenkins/jenkins:lts-jdk11
RUN jenkins-plugin-cli --plugins "maven-plugin:3.20"

# build using docker build -t leo-jenkins:0.0.1 .
