## Run application on docker. Not mandatory, use it if you want
## Commands to run app. Executed in this order.
## docker build -t springio/imatia-state-machine .
## docker run springio/imatia-state-machine .

FROM openjdk:8
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]