### STAGE 1: Cache ###
FROM gradle:7.4.2-jdk18 as cache
RUN mkdir -p /home/gradle/cache_home
ENV GRADLE_USER_HOME /home/gradle/cache_home
COPY build.gradle /home/gradle/java-code/
WORKDIR /home/gradle/java-code

RUN gradle clean build

### STAGE 2: Build ###
FROM gradle:7.4.2-jdk18 as builder
COPY --from=cache /home/gradle/cache_home /home/gradle/.gradle
COPY . /usr/src/java-code/
WORKDIR /usr/src/java-code

RUN gradle bootJar

### STAGE 3: Deploy ###
FROM openjdk:18 as run

WORKDIR /usr/src/java-app

COPY --from=builder /usr/src/java-code/build/libs/*.jar ./app.jar

ENTRYPOINT [ "sh", "-c", "java -XX:+UseSerialGC -Djava.security.egd=file:/dev/./urandom -jar app.jar" ]
