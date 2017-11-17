FROM openjdk:8

ADD build/libs/writersreaderdiscarded.jar /app.jar

ENTRYPOINT ["/usr/bin/java", "-jar", "/app.jar", "http://linkerd:4141/server"]
