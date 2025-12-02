# Imagen base con JDK 21
FROM eclipse-temurin:21-jdk

# Directorio de trabajo
WORKDIR /app

# Copiar archivo JAR generado por Maven o Gradle
COPY target/mi-app.jar app.jar

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
