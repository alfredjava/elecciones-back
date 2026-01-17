# Fase 1: Compilación
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Copiamos todo el contenido del repo a la raíz del contenedor
COPY . .

# Ejecutamos la compilación directamente
RUN mvn clean package -DskipTests

# Fase 2: Ejecución
FROM eclipse-temurin:21-jre-jammy
WORKDIR /deployments

# Copiamos los archivos generados
COPY --from=build /target/quarkus-app/ /deployments/

ENV JAVA_OPTIONS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager -Xmx350m"
EXPOSE 8080
USER 185

ENTRYPOINT [ "java", "-jar", "/deployments/quarkus-run.jar" ]