//package com.alf.elecciones.config;
//
//import org.eclipse.microprofile.config.inject.ConfigProperty;
//import jakarta.ws.rs.container.ContainerRequestContext;
//import jakarta.ws.rs.container.ContainerResponseContext;
//import jakarta.ws.rs.container.ContainerResponseFilter;
//import jakarta.ws.rs.ext.Provider;
//import java.io.IOException;
//
//@Provider
//public class CorsFilter implements ContainerResponseFilter {
//
//    // Inyectamos la variable. Si no existe, por defecto usa localhost
//    @ConfigProperty(name = "quarkus.http.cors.origins", defaultValue = "http://localhost:5173")
//    String allowedOrigin;
//
//    @Override
//    public void filter(ContainerRequestContext requestContext,
//                       ContainerResponseContext responseContext) throws IOException {
//        responseContext.getHeaders().add("Access-Control-Allow-Origin", allowedOrigin);
//        responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
//        responseContext.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
//        responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
//    }
//}