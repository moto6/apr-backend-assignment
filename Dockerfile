FROM eclipse-temurin:21-jdk-jammy
ENV TZ=UTC
ENV SPRING_PROFILES_ACTIVE=local
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", \
            "-Duser.timezone=UTC", \
            "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", \
            "-jar", "/app.jar"]