# Fase 1: Compilación
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# 1. Copiamos el POM que sí está en la raíz
COPY pom.xml .

# 2. CAMBIO AQUÍ: Tu carpeta en GitHub se llama "src/main"
# La copiamos dentro de una carpeta llamada "src" para que Maven sea feliz
COPY src/main ./src/main

# 3. Ejecutamos la compilación
RUN mvn clean package -DskipTests

# Fase 2: Ejecución (Se mantiene igual)
FROM eclipse-temurin:21-jre-jammy
WORKDIR /deployments
COPY --from=build /app/target/quarkus-app/ /deployments/

ENV JAVA_OPTIONS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager -Xmx350m"
EXPOSE 8080
USER 185
ENTRYPOINT [ "java", "-jar", "/deployments/quarkus-run.jar" ]