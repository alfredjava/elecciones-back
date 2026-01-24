package com.alf.elecciones.resources;

import com.alf.elecciones.entities.Candidato;
import com.alf.elecciones.entities.Voto;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.jboss.resteasy.reactive.RestForm;

import java.io.IOException;
import java.io.InputStream;
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

    @POST
    @Path("/importar")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Transactional
    public Response importarExcel(@RestForm("file") InputStream fileInputStream) throws IOException {
        Workbook workbook = new XSSFWorkbook(fileInputStream);
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Saltar encabezado

            Candidato c = new Candidato();
            c.nombre = row.getCell(0).getStringCellValue();
            c.partido = row.getCell(1).getStringCellValue();
            c.fotoUrl = row.getCell(2).getStringCellValue();
            c.propuesta = row.getCell(3).getStringCellValue();

            c.persist();
        }
        workbook.close();
        return Response.ok("Candidatos cargados con éxito").build();
    }
}
