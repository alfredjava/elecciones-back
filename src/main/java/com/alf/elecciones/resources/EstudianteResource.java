package com.alf.elecciones.resources;

import com.alf.elecciones.entities.Estudiante;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/v1/estudiantes") // <-- VITAL: Sin esto Swagger no lo ve
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EstudianteResource {

    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") Long id) {
        // findById es un método estático de PanacheEntity
        Estudiante estudiante = Estudiante.findById(id);

        if (estudiante == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Estudiante no encontrado").build();
        }

        return Response.ok(estudiante).build();
    }

    @GET
    @Path("/carnet/{carnet}")
    public Estudiante obtenerPorCarnet(@PathParam("carnet") String carnet) {
        return Estudiante.findByCarnet(carnet);
    }
}
