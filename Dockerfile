FROM mirror.gcr.io/library/eclipse-temurin:21-jre-alpine

COPY rutok*service/target/*.jar service.jar

EXPOSE 10001

CMD exec java $JAVA_OPTS -jar service.jar