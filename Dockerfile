# Fase de construcción
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /home/app

# 1. Copia el archivo de configuración de dependencias
COPY pom.xml .

# 2. Copia TODA la carpeta src (que contiene a main y todo lo demás)
COPY src ./src

# 3. Compila el proyecto
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