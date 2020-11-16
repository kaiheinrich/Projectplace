FROM openjdk:15-oracle

ENV ENVIRONMENT=prod

MAINTAINER Kai Heinrich <kai.heinrich91@web.de>

ADD backend/target/projectplace.jar app.jar

CMD [ "sh", "-c", "java -jar /app.jar" ]