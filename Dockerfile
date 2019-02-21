FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8082
ADD /target/therapist-area-0.0.1-SNAPSHOT.jar therapist-area.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /therapist-area.jar" ]