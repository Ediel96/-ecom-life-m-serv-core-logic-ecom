FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace

# Copia archivos clave para cachear dependencias
COPY settings.gradle build.gradle gradlew ./
COPY gradle ./gradle
RUN chmod +x gradlew

# Pre-resuelve dependencias (cache)
RUN ./gradlew --no-daemon dependencies || true

# Copia el código (incluye api.yaml dentro de src/main/resources)
COPY src ./src

# Genera OpenAPI y construye el jar
RUN ./gradlew --no-daemon openApiGenerate bootJar -x test --stacktrace

FROM eclipse-temurin:20-jre
WORKDIR /app

COPY --from=build /workspace/build/libs/*.jar app.jar

EXPOSE 8083

# No pongas secretos aquí. Pásalos por docker-compose o variables en tiempo de ejecución.
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
ENV SERVER_PORT=8083

# Base de datos: por defecto apunta al host de macOS. Sobrescribe en tiempo de ejecución.
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/life_organizer
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=your_password

# dockerfile
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.address=0.0.0.0 -Dserver.port=${SERVER_PORT} -jar /app/app.jar"]