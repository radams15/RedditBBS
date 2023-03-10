FROM docker.io/eclipse-temurin:11-jre

RUN mkdir -p /app

ENV _JAVA_OPTIONS="-Djava.net.preferIPv4Stack=true"

COPY ./target/RedditBBS-0.1.jar /app/RedditBBS.jar

WORKDIR /app

ENTRYPOINT ["java", "-jar", "RedditBBS.jar"]