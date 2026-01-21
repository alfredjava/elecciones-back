package com.alf.elecciones.resources;

import com.alf.elecciones.entities.Estudiante;
import jakarta.transaction.Transactional;
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

    @POST
    @Transactional
    public Response registrarEstudiante(Estudiante nuevoEstudiante) {
        // 1. Validar si el carnet ya existe
        if (Estudiante.findByCarnet(nuevoEstudiante.carnet) != null) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("El carnet " + nuevoEstudiante.carnet + " ya está registrado.")
                    .build();
        }

        // 2. Validar si el documento de identidad ya existe
        // Nota: Asegúrate de haber agregado findByDocumento en tu Entity
        if (Estudiante.find("documentoIdentidad", nuevoEstudiante.documentoIdentidad).firstResult() != null) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("El documento de identidad ya está registrado.")
                    .build();
        }

        // 3. Si todo está bien, guardar
        nuevoEstudiante.yaVoto = false; // Forzamos que inicie sin haber votado
        nuevoEstudiante.persist();

        return Response.status(Response.Status.CREATED).entity(nuevoEstudiante).build();
    }
}
