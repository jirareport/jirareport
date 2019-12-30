FROM openjdk:12 as builder

COPY . /jirareport-api
WORKDIR /jirareport-api

RUN ./gradlew unitTest
RUN ./gradlew build -x test

###

FROM openjdk:12-alpine
EXPOSE 80

COPY --from=builder /jirareport-api/build/libs/jirareport.jar /usr/src/
WORKDIR /usr/src/

CMD java $JAVA_OPTS -Dserver.port=80 -jar jirareport.jar
