# Fase 1: Compilación
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copiamos todo el contenido del repositorio
COPY . .

# Ejecutamos la compilación
RUN mvn clean package -DskipTests

# Fase 2: Ejecución
FROM eclipse-temurin:21-jre-jammy
WORKDIR /deployments

# CORRECCIÓN: Copiamos el contenido INTERNO de quarkus-app directamente a /deployments
# El punto (.) al final indica que se copie el contenido, no la carpeta entera
COPY --from=build /app/target/quarkus-app/ .

ENV JAVA_OPTIONS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager -Xmx350m"
EXPOSE 8080

# Usuario con permisos limitados para seguridad en la nube
USER 185

# Ahora el JAR está en la raíz de /deployments y se puede ejecutar directamente
ENTRYPOINT [ "java", "-jar", "quarkus-run.jar" ]