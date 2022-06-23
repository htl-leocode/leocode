#sudo su
docker run -d -v jenkins_home:/var/jenkins_home -p 8081:8080 -p 50000:50000 --name jenkins jenkins/jenkins:lts-jdk11
docker exec -it -u root jenkins /bin/bash
apt update && apt install maven -y
echo initialAdminPassword:
cat /var/jenkins_home/secrets/initialAdminPassword
exit
