# Fase de construcción con Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copiamos ABSOLUTAMENTE TODO lo que hay en el repo al contenedor
# Esto garantiza que encuentre el pom.xml y la carpeta src/main
COPY . .

# Ejecutamos la compilación
RUN mvn clean package -DskipTests

# Fase de ejecución
FROM eclipse-temurin:21-jre-jammy
WORKDIR /deployments

# Copiamos los resultados
COPY --from=build /app/target/quarkus-app/lib/ /deployments/lib/
COPY --from=build /app/target/quarkus-app/*.jar /deployments/
COPY --from=build /app/target/quarkus-app/app/ /deployments/app/
COPY --from=build /app/target/quarkus-app/quarkus/ /deployments/quarkus/

ENV JAVA_OPTIONS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager -Xmx350m"
EXPOSE 8080
USER 185
ENTRYPOINT [ "java", "-jar", "/deployments/quarkus-run.jar" ]