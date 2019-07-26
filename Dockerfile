FROM openjdk:8 as builder

COPY . /jirareport-api
WORKDIR /jirareport-api

RUN ./gradlew -i unitTest

COPY ./src/main/resources/application.yml /jirareport-api/src/main/resources/application.yml
RUN ./gradlew -i build -x test

###

FROM openjdk:8-jre-slim
EXPOSE 80

COPY --from=builder /jirareport-api/build/libs/jirareport.jar /usr/src/
WORKDIR /usr/src/

CMD java $JAVA_OPTS -Dserver.port=80 -jar jirareport.jar
