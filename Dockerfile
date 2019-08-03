FROM openjdk:8 as builder

COPY . /jirareport-api
WORKDIR /jirareport-api

RUN ./gradlew build

###

FROM openjdk:8-jre-slim
EXPOSE 80

COPY --from=builder /jirareport-api/build/libs/jirareport.jar /usr/src/
WORKDIR /usr/src/

CMD java $JAVA_OPTS -Dserver.port=80 -jar jirareport.jar
