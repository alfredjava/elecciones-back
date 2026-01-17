# Fase 1: Compilación
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copiamos ABSOLUTAMENTE TODO el repositorio al contenedor
COPY . .

# Comando de diagnóstico: Esto imprimirá en el log qué carpetas hay
RUN ls -R

# Ejecutamos la compilación. Maven buscará el pom.xml en la raíz de /app
RUN mvn clean package -DskipTests

# Fase 2: Ejecución
FROM eclipse-temurin:21-jre-jammy
WORKDIR /deployments

# Copiamos el resultado de la compilación de Quarkus
COPY --from=build /app/target/quarkus-app/ /deployments/

ENV JAVA_OPTIONS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager -Xmx350m"
EXPOSE 8080
USER 185

ENTRYPOINT [ "java", "-jar", "/deployments/quarkus-run.jar" ]