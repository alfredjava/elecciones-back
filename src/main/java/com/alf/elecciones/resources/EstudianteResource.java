package com.alf.elecciones.resources;

import com.alf.elecciones.entities.Candidato;
import com.alf.elecciones.entities.Estudiante;
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
import java.util.List;

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

    @POST
    @Path("/importar")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Transactional
    public Response importarEstudiantesExcel(@RestForm("file") InputStream fileInputStream) throws IOException {
        Workbook workbook = new XSSFWorkbook(fileInputStream);
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Saltar encabezado

            String carnet = row.getCell(0).getStringCellValue();

            // Evitar duplicados: Si ya existe el carnet, no lo creamos de nuevo
            if (Estudiante.find("carnet", carnet).firstResult() == null) {
                Estudiante e = new Estudiante();
                e.carnet = carnet;
                e.documentoIdentidad = row.getCell(1).getStringCellValue(); // Nuevo campo
                e.nombre = row.getCell(2).getStringCellValue();
                e.apellidos = row.getCell(3).getStringCellValue();
                e.facultad = row.getCell(4).getStringCellValue();
                e.yaVoto = false; // Valor inicial obligatorio
                e.persist();
            }
        }
        workbook.close();
        return Response.ok("Padrón electoral cargado con éxito").build();
    }

    @GET
    @Operation(summary = "Lista todos los estudiantes")
    public List<Estudiante> listarTodos() {
        // listAll() es un método estático de PanacheEntity
        return Estudiante.listAll();
    }

    @GET
    @Path("/buscar/{documento}")
    public Response buscarPorDocumento(@PathParam("documento") String documento) {
        Estudiante est = Estudiante.find("documentoIdentidad", documento).firstResult();
        if (est == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("No estás en el padrón electoral").build();
        }
        if (est.yaVoto) {
            return Response.status(Response.Status.FORBIDDEN).entity("Ya has emitido tu voto").build();
        }
        return Response.ok(est).build();
    }
}
