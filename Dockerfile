FROM openjdk:13 as builder

COPY . /jirareport-api
WORKDIR /jirareport-api

RUN ./gradlew clean build -x test

###

FROM openjdk:13-alpine
EXPOSE 80 443

COPY --from=builder /jirareport-api/build/libs/jirareport.jar /usr/src/
WORKDIR /usr/src/

CMD java $JAVA_OPTS -Dserver.port=80 -jar jirareport.jar
