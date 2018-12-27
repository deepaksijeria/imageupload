Pre-requisite
1. Install Jdk 1.8
2. Install maven
3. Install docker

Steps to execute

1. Go to project directory containing pom.xml and DockerFile

2.Build project using below command

mvn clean install

3.Execute below command to build docker image

docker build -f Dockerfile -t imageupload .

4.Step3 will create a docker image with name imageupload which reside in docker

5.Execute the below command to start the application

docker run -p 8080:8080 imageupload


Alternative to start application :

1. Build project using the below command
mvn clean install

2. Start application using the below command
mvn spring-boot:run