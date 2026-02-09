FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace

COPY settings.gradle build.gradle gradlew ./
COPY gradle ./gradle
RUN chmod +x gradlew

RUN ./gradlew --no-daemon dependencies || true

COPY src ./src

RUN ./gradlew --no-daemon openApiGenerate bootJar -x test --stacktrace

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /workspace/build/libs/*.jar app.jar

EXPOSE 8083

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
ENV SERVER_PORT=8082

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.address=0.0.0.0 -Dserver.port=${SERVER_PORT} -jar /app/app.jar"]