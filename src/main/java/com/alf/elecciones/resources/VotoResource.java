package com.alf.elecciones.resources;

import com.alf.elecciones.dto.VotoRequest;
import com.alf.elecciones.entities.Candidato;
import com.alf.elecciones.entities.Estudiante;
import com.alf.elecciones.entities.Voto;
import io.vertx.core.http.HttpServerRequest;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;


@Path("/api/v1/votos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VotoResource {

    @POST
    @Transactional
    @Operation(summary = "Registra un voto", operationId = "registrarVoto")
    public Response registrarVoto(VotoRequest request, @Context HttpServerRequest postmanRequest) {
        // 1. Validar si el estudiante existe
        Estudiante estudiante = Estudiante.findById(request.estudianteId);
        if (estudiante == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Estudiante no encontrado").build();
        }

        // 2. Validar si ya votó (Seguridad lógica)
        if (estudiante.yaVoto) {
            return Response.status(Response.Status.CONFLICT).entity("El estudiante ya ejerció su voto").build();
        }

        // 3. Validar si el candidato existe
        Candidato candidato = Candidato.findById(request.candidatoId);
        if (candidato == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Candidato no encontrado").build();
        }
        // 4. Obtener la IP desde el contexto del servidor
        String ipReal = postmanRequest.getHeader("X-Forwarded-For");
        if (ipReal == null || ipReal.isEmpty()) {
            ipReal = postmanRequest.remoteAddress().host();
        }

        // 5. Registrar el voto
        Voto nuevoVoto = new Voto();
        nuevoVoto.estudiante = estudiante;
        nuevoVoto.candidato = candidato;
        nuevoVoto.ipOrigen = ipReal;
        nuevoVoto.persist();

        // 5. Marcar al estudiante como "ya votó"
        estudiante.yaVoto = true;
        estudiante.persist();

        return Response.status(Response.Status.CREATED).entity("Voto registrado con éxito").build();
    }
}
