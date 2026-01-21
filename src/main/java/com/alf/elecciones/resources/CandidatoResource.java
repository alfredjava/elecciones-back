package com.alf.elecciones.resources;

import com.alf.elecciones.entities.Candidato;
import com.alf.elecciones.entities.Voto;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/api/v1/candidatos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CandidatoResource {

    @GET
    @Path("/{id}")
    @Operation(summary = "Obtiene un candidato por su ID")
    public Response obtenerCandidato(@PathParam("id") Long id) {
        // Uso directo del método heredado de PanacheEntity
        Candidato candidato = Candidato.findById(id);

        if (candidato == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("El candidato con ID " + id + " no existe.")
                    .build();
        }

        return Response.ok(candidato).build();
    }

    @GET
    @Path("/resultados")
    @Operation(summary = "Obtiene el conteo de votos por candidato para las gráficas")
    public List<Map<String, Object>> obtenerResultados() {
        // Agrupamos por nombre y partido para mayor detalle en el frontend
        List<Object[]> resultados = Voto.getEntityManager()
                .createQuery("SELECT v.candidato.nombre, v.candidato.partido, COUNT(v) " +
                        "FROM Voto v GROUP BY v.candidato.nombre, v.candidato.partido")
                .getResultList();

        return resultados.stream().map(fila -> {
            Map<String, Object> map = new HashMap<>();
            map.put("nombre", fila[0]);
            map.put("partido", fila[1]);
            map.put("votos", fila[2]);
            return map;
        }).collect(Collectors.toList());
    }

    @GET
    @Operation(summary = "Lista todos los candidatos")
    public List<Candidato> listarTodos() {
        // listAll() es un método estático de PanacheEntity
        return Candidato.listAll();
    }
}
