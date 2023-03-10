FROM docker.io/eclipse-temurin:11-jre

COPY target/RedditBBS-*.jar /app/RedditBBS.jar

WORKDIR /app

ENTRYPOINT ["java", "-jar", "RedditBBS.jar"]