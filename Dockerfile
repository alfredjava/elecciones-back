# 1. Etapa de construcción (Build) con Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /home/app

# Copiar pom.xml y descargar dependencias para aprovechar la caché
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiar código y compilar
COPY src/main ./src/main
RUN mvn package -DskipTests

# 2. Etapa de ejecución (Run) con JRE 21
FROM eclipse-temurin:21-jre-jammy
WORKDIR /deployments

# Copiamos los artefactos generados por Quarkus
COPY --from=build /home/app/target/quarkus-app/lib/ /deployments/lib/
COPY --from=build /home/app/target/quarkus-app/*.jar /deployments/
COPY --from=build /home/app/target/quarkus-app/app/ /deployments/app/
COPY --from=build /home/app/target/quarkus-app/quarkus/ /deployments/quarkus/

# Variables de entorno para Render
ENV JAVA_OPTIONS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager -Xmx350m"
EXPOSE 8080

USER 185

ENTRYPOINT [ "java", "-jar", "/deployments/quarkus-run.jar" ]