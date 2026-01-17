# Fase 1: Compilación
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Copiamos todo el contenido del repo a una carpeta temporal
COPY . /app-source
WORKDIR /app-source

# Ejecutamos Maven directamente donde acabamos de copiar todo
RUN mvn clean package -DskipTests

# Fase 2: Ejecución (Se mantiene igual)
FROM eclipse-temurin:21-jre-jammy
WORKDIR /deployments

# Copiamos desde la carpeta /app-source de la fase anterior
COPY --from=build /app-source/target/quarkus-app/lib/ /deployments/lib/
COPY --from=build /app-source/target/quarkus-app/*.jar /deployments/
COPY --from=build /app-source/target/quarkus-app/app/ /deployments/app/
COPY --from=build /app-source/target/quarkus-app/quarkus/ /deployments/quarkus/

ENV JAVA_OPTIONS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager -Xmx350m"
EXPOSE 8080
USER 185
ENTRYPOINT [ "java", "-jar", "/deployments/quarkus-run.jar" ]