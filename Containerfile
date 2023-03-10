FROM docker.io/eclipse-temurin:11-jre

RUN mkdir -p /app

COPY ./target/RedditBBS-0.1.jar /app/RedditBBS.jar

WORKDIR /app

ENTRYPOINT ["java", "-jar", "RedditBBS.jar"]