FROM openjdk:15-oracle

ENV ENVIRONMENT=prod

MAINTAINER Kai Heinrich <kai.heinrich91@web.de>

ADD backend/target/projectplace.jar app.jar

CMD ["sh", "-c", "java -jar -Dserver.port=$PORT -Dspring.data.mongodb.uri=$MONGO_DB_URI -Djwt.secretkey=$JWT_SECRETKEY -Daws.access.key=$AWS_ACCESS_KEY -Daws.secret.key=$AWS_SECRET_KEY -Daws.bucket.name=$AWS_BUCKET_NAME app.jar" ]