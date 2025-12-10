# backend/Dockerfile
FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace

# copia todo y build (usa tu wrapper mvnw)
COPY . .
RUN ./mvnw -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
