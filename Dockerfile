# Fase 1: Compilación
FROM maven:3.9.6-eclipse-temurin-21 AS build

# DEFINIR DIRECTORIO DE TRABAJO ES CLAVE
WORKDIR /app

# Primero copiamos el pom.xml para asegurar que Maven lo vea
COPY pom.xml .

# Luego copiamos la carpeta src completa
COPY src ./src

# Ejecutamos la compilación desde /app donde está el pom.xml
RUN mvn clean package -DskipTests

# Fase 2: Ejecución
FROM eclipse-temurin:21-jre-jammy
WORKDIR /deployments

# Copiamos los archivos generados desde la fase build
COPY --from=build /app/target/quarkus-app/ /deployments/

ENV JAVA_OPTIONS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager -Xmx350m"
EXPOSE 8080
USER 185

ENTRYPOINT [ "java", "-jar", "/deployments/quarkus-run.jar" ]