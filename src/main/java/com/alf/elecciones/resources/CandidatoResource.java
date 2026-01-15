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
        // Consulta JPQL para agrupar votos por candidato
        // v.candidato.nombre es el camino a través de la relación @ManyToOne
        List<Object[]> resultados = Voto.getEntityManager()
                .createQuery("SELECT v.candidato.nombre, COUNT(v) FROM Voto v GROUP BY v.candidato.nombre")
                .getResultList();

        // Transformamos a una lista de mapas para que el JSON sea fácil de leer en React
        return resultados.stream().map(fila -> {
            Map<String, Object> map = new HashMap<>();
            map.put("nombre", fila[0]);
            map.put("votos", fila[1]);
            return map;
        }).collect(Collectors.toList());
    }
}
