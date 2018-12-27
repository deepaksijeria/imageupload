FROM openjdk:8
ADD target/imageupload.jar imageupload.jar
ENTRYPOINT ["java","-jar","imageupload.jar"]