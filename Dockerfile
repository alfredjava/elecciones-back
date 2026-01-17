# Fase 1: Compilación
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Fase 2: Ejecución
FROM eclipse-temurin:21-jre-jammy
WORKDIR /deployments

# Copiamos el contenido directamente
COPY --from=build /app/target/quarkus-app/ .

ENV JAVA_OPTIONS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager -Xmx350m"
EXPOSE 8080
USER 185

# Ejecución directa del jar en la raíz de /deployments
ENTRYPOINT [ "java", "-jar", "quarkus-run.jar" ]