package com.ipartek.controlador;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ipartek.auxiliar.Auxiliar;
import com.ipartek.modelo.Artista;
import com.ipartek.modelo.Disco;
import com.ipartek.modelo.Discografica;
import com.ipartek.pojos.ErrorMsg;
import com.ipartek.servicios.DiscograficaServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/discograficas")
@Tag(name = "Discograficas", description = "Operaciones relacionadas con las discograficas")
public class DiscograficaControladorREST {
	
	@Value("${ruta.imagenes.logos}") // Inyecta la ruta de imágenes desde application.properties
	String rutaFotos;
	
	@Autowired
	private DiscograficaServicio discograficaServicio;
	
	@GetMapping("")
	@Operation(summary = "Obtener todas las discograficas")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Discograficas obtenidas", 
			content = @Content(array = @ArraySchema(schema = @Schema(implementation = Artista.class)))), 
		@ApiResponse(responseCode = "404", description = "Discografica está vacía",
			content = @Content(schema = @Schema(implementation =  ErrorMsg.class)))
	})
	public ResponseEntity<Object> obtenerTodasDiscograficas() {
		
		List<Discografica> discografica = discograficaServicio.obtenerTodasDiscograficas();

		if (discografica.size()!=0) {//200
			return ResponseEntity.ok().body(discografica);
		}
		else {//404
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMsg(1, "No se pudo encontar la discografica"));
		}
	}

	
	@GetMapping("/{id}")
	@Operation(summary = "Obtener una discografica por su id en la barra de direcciones")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Discografica obtenida", 
			content = @Content(schema = @Schema(implementation =  Artista.class))), 
		@ApiResponse(responseCode = "404", description = "Discografica NO obtenida", 
			content = @Content(schema = @Schema(implementation =  ErrorMsg.class))),
		@ApiResponse(responseCode = "400", description = "Parametro mal puesto", 
			content = @Content(schema = @Schema(implementation =  ErrorMsg.class)))
	})
	public ResponseEntity<Object> obtenerDiscograficaPorId(@PathVariable String id) {
		try { 
			int idTemp = Integer.parseInt(id.toString());
			
			Discografica discografica = discograficaServicio.obtenerDiscograficaPorID(idTemp);
			
			if (discografica.getId()!=0) {//200
				return ResponseEntity.ok().body(discografica);
			}
			else {//404
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMsg(1, "No se pudo encontar la discografica"));
			}
		} catch (Exception e) {//400
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMsg(1, "ID con formato no valido"));
		}
	}
	
	
	@GetMapping("/{id}/discografia")
	@Operation(summary = "Obtener discografía de una discográfica por su ID")
	@ApiResponses({
	    @ApiResponse(responseCode = "200", description = "Discografía obtenida correctamente",
	        content = @Content(array = @ArraySchema(schema = @Schema(implementation = Disco.class)))),
	    @ApiResponse(responseCode = "404", description = "Discográfica no encontrada",
	        content = @Content(schema = @Schema(implementation = ErrorMsg.class))),
	    @ApiResponse(responseCode = "400", description = "Parámetro ID mal formado",
	        content = @Content(schema = @Schema(implementation = ErrorMsg.class))),
	    @ApiResponse(responseCode = "500", description = "Error interno del servidor",
	        content = @Content(schema = @Schema(implementation = ErrorMsg.class)))
	})
	public ResponseEntity<Object> obtenerDiscografiaPorDiscografica(@PathVariable String id) {
	    try {
	        int idInt = Integer.parseInt(id);

	        // Llamada al servicio
	        List<Disco> discografia = discograficaServicio.obtenerDiscografiaPorDiscografica(idInt);

	        if (discografia == null) {
	            // Si el servicio devuelve null, la discográfica no existe
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(new ErrorMsg(1, "La discográfica no existe."));
	        }

	        // Si existe la discográfica pero no tiene discos → devolver lista vacía (no error)
	        return ResponseEntity.ok(discografia);

	    } catch (NumberFormatException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	            .body(new ErrorMsg(1, "El ID de la discográfica no tiene un formato válido."));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body(new ErrorMsg(2, "Error interno del servidor: " + e.getMessage()));
	    }
	}
	
	
	@PostMapping()
	@Operation(summary = "Insertar una discográfica")
	@ApiResponses({
	    @ApiResponse(responseCode = "200", description = "Discográfica guardada"), 
	    @ApiResponse(responseCode = "500", description = "Discográfica NO guardada")
	})
	public ResponseEntity<ErrorMsg> insertarDiscografica(
	        @RequestPart("discografica") Discografica discografica,
	        @RequestPart(name = "foto2", required = false) MultipartFile archivo) {

	    try {
	        // Guardar el logo si se ha enviado
	        if (archivo != null && !archivo.isEmpty()) {
	            String nombreArchivo = Auxiliar.guardarImagen(archivo, rutaFotos);
	            discografica.setLogo(nombreArchivo);
	        } else {
	            discografica.setLogo("default.png");
	        }

	        boolean insertado = discograficaServicio.insertarDiscografica(discografica);

	        if (insertado) {
	            return ResponseEntity.ok(new ErrorMsg(0, "Discográfica insertada correctamente"));
	        } else {
	            return ResponseEntity.internalServerError().body(new ErrorMsg(1, "No se pudo insertar la discográfica"));
	        }

	    } catch (IOException e) {
	        return ResponseEntity.internalServerError().body(new ErrorMsg(1, "Error al guardar el logo"));
	    }
	}

	
	@DeleteMapping("/{id}")
	@Operation(summary = "Borrar una discografica por su id en la barra de direcciones" )
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Discografica obtenida"), 		
		@ApiResponse(responseCode = "404", description = "Discografica NO obtenida"), 	
		@ApiResponse(responseCode = "500", description = "Error") 		
	})
	public ResponseEntity<ErrorMsg> borrarUnaDiscografica(@PathVariable String id) {
		try { 
			int idTemp = Integer.parseInt(id.toString());
			
			boolean resultado = discograficaServicio.borrarDiscografica(idTemp);
			
			if (resultado) {//200
				return ResponseEntity.ok().body(new ErrorMsg(0, "Discografica borrada correctamente"));
			}
			else {//500
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMsg(1, "No se pudo borrar la discografica"));
			}
		} catch (Exception e) {//400
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMsg(1, "ID con formato no valido"));
		}
	}
	
	
	@PutMapping()
	@Operation(summary = "Modificar una discografica")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Discografica modificada", 
			content = @Content(schema = @Schema(implementation =  ErrorMsg.class))), 
		@ApiResponse(responseCode = "500", description = "No se pudo modificar",
			content = @Content(schema = @Schema(implementation =  ErrorMsg.class)))
	})
	public ResponseEntity<ErrorMsg> modificarDiscografica(@RequestBody Discografica discografica) {
		boolean modificado = discograficaServicio.modificarDiscografica(discografica);
		
		if (modificado) {
			return ResponseEntity.ok().body(new ErrorMsg(0, "Discografica modificada correctamente"));
		}else {
			return ResponseEntity.internalServerError().body(new ErrorMsg(1, "No se pudo modificar la discografica"));
		}
	}

}

