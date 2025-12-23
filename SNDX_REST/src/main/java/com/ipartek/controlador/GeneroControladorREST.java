package com.ipartek.controlador;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ipartek.modelo.Disco;
import com.ipartek.modelo.Genero;
import com.ipartek.pojos.ErrorMsg;
import com.ipartek.servicios.GeneroServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/generos")
@Tag(name = "Géneros", description = "Operaciones relacionadas con los géneros")
public class GeneroControladorREST {

    @Autowired
    private GeneroServicio generoServicio;

    @GetMapping("")
    @Operation(summary = "Obtener todos los géneros")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Géneros obtenidos",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Genero.class)))),
        @ApiResponse(responseCode = "404", description = "No se encontraron géneros",
            content = @Content(schema = @Schema(implementation = ErrorMsg.class)))
    })
    public ResponseEntity<Object> obtenerTodosGeneros() {
        List<Genero> generos = generoServicio.obtenerTodosGeneros();

        if (!generos.isEmpty()) {
            return ResponseEntity.ok().body(generos);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorMsg(1, "No se pudieron encontrar géneros"));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un género por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Género obtenido",
            content = @Content(schema = @Schema(implementation = Genero.class))),
        @ApiResponse(responseCode = "404", description = "Género no encontrado",
            content = @Content(schema = @Schema(implementation = ErrorMsg.class))),
        @ApiResponse(responseCode = "400", description = "ID con formato inválido",
            content = @Content(schema = @Schema(implementation = ErrorMsg.class)))
    })
    public ResponseEntity<Object> obtenerGeneroPorId(@PathVariable String id) {
        try {
            int idInt = Integer.parseInt(id);

            Genero genero = generoServicio.obtenerGeneroPorID(idInt);

            if (genero != null && genero.getId() != 0) {
                return ResponseEntity.ok().body(genero);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorMsg(1, "No se pudo encontrar el género"));
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMsg(1, "ID con formato no válido"));
        }
    }

    @GetMapping("/{id}/discos")
    @Operation(summary = "Obtener discos asociados a un género por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Discos obtenidos",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Disco.class)))),
        @ApiResponse(responseCode = "404", description = "Género no encontrado o sin discos",
            content = @Content(schema = @Schema(implementation = ErrorMsg.class))),
        @ApiResponse(responseCode = "400", description = "ID con formato inválido",
            content = @Content(schema = @Schema(implementation = ErrorMsg.class)))
    })
    public ResponseEntity<Object> obtenerDiscosPorGenero(@PathVariable String id) {
        try {
            int idInt = Integer.parseInt(id);

            List<Disco> discografia = generoServicio.obtenerDiscografiaPorGenero(idInt);

            if (discografia != null && !discografia.isEmpty()) {
                return ResponseEntity.ok().body(discografia);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorMsg(1, "El género no tiene discos o no existe"));
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMsg(1, "ID con formato no válido"));
        }
    }

    @PostMapping("")
    @Operation(summary = "Insertar un género")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Género insertado"),
        @ApiResponse(responseCode = "500", description = "Error al insertar el género")
    })
    public ResponseEntity<ErrorMsg> insertarGenero(@RequestBody Genero genero) {
        boolean insertado = generoServicio.insertarGenero(genero);

        if (insertado) {
            return ResponseEntity.ok().body(new ErrorMsg(0, "Género insertado correctamente"));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMsg(1, "No se pudo insertar el género"));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Borrar un género por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Género borrado"),
        @ApiResponse(responseCode = "404", description = "Género no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error al borrar el género"),
        @ApiResponse(responseCode = "400", description = "ID con formato inválido")
    })
    public ResponseEntity<ErrorMsg> borrarGenero(@PathVariable String id) {
        try {
            int idInt = Integer.parseInt(id);

            boolean resultado = generoServicio.borrarGenero(idInt);

            if (resultado) {
                return ResponseEntity.ok().body(new ErrorMsg(0, "Género borrado correctamente"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorMsg(1, "No se pudo borrar el género"));
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMsg(1, "ID con formato no válido"));
        }
    }

    @PutMapping("")
    @Operation(summary = "Modificar un género")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Género modificado"),
        @ApiResponse(responseCode = "500", description = "Error al modificar el género")
    })
    public ResponseEntity<ErrorMsg> modificarGenero(@RequestBody Genero genero) {
        boolean modificado = generoServicio.modificarGenero(genero);

        if (modificado) {
            return ResponseEntity.ok().body(new ErrorMsg(0, "Género modificado correctamente"));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMsg(1, "No se pudo modificar el género"));
        }
    }
}

