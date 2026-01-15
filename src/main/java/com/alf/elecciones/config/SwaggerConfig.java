package com.alf.elecciones.config;

import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;

@OpenAPIDefinition(
        info = @Info(
                title = "API de Sistema de Elecciones Estudiantiles",
                version = "1.0.0",
                contact = @Contact(
                        name = "Alfredo Soporte",
                        url = "http://alf.com/soporte",
                        email = "alf@example.com"),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"),
                description = "API para gestionar candidatos, estudiantes y el registro de votos en tiempo real.")
)
public class SwaggerConfig extends Application {
    // No necesitas escribir código aquí, las anotaciones hacen el trabajo.
}
