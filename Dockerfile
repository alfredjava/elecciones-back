# Fase 1: Compilación
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copiamos todo el repo
COPY . .

# Este comando buscará dónde está el pom.xml y se moverá ahí para compilar
RUN POM_PATH=$(find . -name "pom.xml" | head -n 1 | xargs dirname) && \
    cd $POM_PATH && \
    mvn clean package -DskipTests && \
    mkdir -p /app/target-final && \
    cp -r target/quarkus-app/* /app/target-final/

# Fase 2: Ejecución
FROM eclipse-temurin:21-jre-jammy
WORKDIR /deployments

# Copiamos desde la carpeta final que creamos arriba
COPY --from=build /app/target-final/ /deployments/

ENV JAVA_OPTIONS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager -Xmx350m"
EXPOSE 8080
USER 185
ENTRYPOINT [ "java", "-jar", "/deployments/quarkus-run.jar" ]