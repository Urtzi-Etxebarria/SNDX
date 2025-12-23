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
import com.ipartek.modelo.Productor;
import com.ipartek.pojos.ErrorMsg;
import com.ipartek.servicios.ProductorServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/productores")
@Tag(name = "Productores", description = "Operaciones relacionadas con los productores")
public class ProductorControladorREST {
	
	@Value("${ruta.imagenes.productores}") // Inyecta la ruta de imágenes desde application.properties
	String rutaFotos;
	
	@Autowired
	private ProductorServicio productorServicio;
	
	@GetMapping("")
	@Operation(summary = "Obtener todos los productores")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Productores obtenidos", 
			content = @Content(array = @ArraySchema(schema = @Schema(implementation = Artista.class)))), 
		@ApiResponse(responseCode = "404", description = "Productor está vacío",
			content = @Content(schema = @Schema(implementation =  ErrorMsg.class)))
	})
	public ResponseEntity<Object> obtenerTodosProductores() {
		
		List<Productor> productor = productorServicio.obtenerTodosProductores();

		if (productor.size()!=0) {//200
			return ResponseEntity.ok().body(productor);
		}
		else {//404
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMsg(1, "No se pudo encontar el productor"));
		}
	}

	
	@GetMapping("/{id}")
	@Operation(summary = "Obtener un productor por su id en la barra de direcciones")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Productor obtenido", 
			content = @Content(schema = @Schema(implementation =  Artista.class))), 
		@ApiResponse(responseCode = "404", description = "Productor NO obtenido", 
			content = @Content(schema = @Schema(implementation =  ErrorMsg.class))),
		@ApiResponse(responseCode = "400", description = "Parametro mal puesto", 
			content = @Content(schema = @Schema(implementation =  ErrorMsg.class)))
	})
	public ResponseEntity<Object> obtenerProductorPorId(@PathVariable String id) {
		try { 
			int idTemp = Integer.parseInt(id.toString());
			
			Productor productor = productorServicio.obtenerProductorPorID(idTemp);
			
			if (productor.getId()!=0) {//200
				return ResponseEntity.ok().body(productor);
			}
			else {//404
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMsg(1, "No se pudo encontar el productor"));
			}
		} catch (Exception e) {//400
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMsg(1, "ID con formato no valido"));
		}
	}
	
	
	@GetMapping("/{id}/discografia")
	@Operation(summary = "Obtener discografía de un productor por su ID")
	@ApiResponses({
	    @ApiResponse(responseCode = "200", description = "Discografía obtenida correctamente",
	        content = @Content(array = @ArraySchema(schema = @Schema(implementation = Disco.class)))),
	    @ApiResponse(responseCode = "404", description = "Productor no encontrado o sin discografía",
	        content = @Content(schema = @Schema(implementation = ErrorMsg.class))),
	    @ApiResponse(responseCode = "400", description = "Parámetro ID mal formado",
	        content = @Content(schema = @Schema(implementation = ErrorMsg.class)))
	})
	public ResponseEntity<Object> obtenerDiscografiaPorProductor(@PathVariable String id) {
	    try {
	        int idInt = Integer.parseInt(id);

	        List<Disco> discografia = productorServicio.obtenerDiscografiaPorProductor(idInt);

	        if (discografia != null && !discografia.isEmpty()) {
	            return ResponseEntity.ok(discografia); // 200
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(new ErrorMsg(1, "El productor no tiene discos o no existe.")); // 404
	        }

	    } catch (NumberFormatException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	            .body(new ErrorMsg(1, "El ID del productor no tiene un formato válido.")); // 400
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body(new ErrorMsg(2, "Error interno del servidor: " + e.getMessage()));
	    }
	}
	
	
	@PostMapping()
	@Operation(summary = "Insertar un productor")
	@ApiResponses({
	    @ApiResponse(responseCode = "200", description = "Productor insertado correctamente",
	        content = @Content(schema = @Schema(implementation = ErrorMsg.class))),
	    @ApiResponse(responseCode = "400", description = "Datos del productor no válidos",
	        content = @Content(schema = @Schema(implementation = ErrorMsg.class))),
	    @ApiResponse(responseCode = "500", description = "Error interno del servidor",
	        content = @Content(schema = @Schema(implementation = ErrorMsg.class)))
	})
	public ResponseEntity<ErrorMsg> insertarProductor(
	        @RequestPart("productor") Productor productor,
	        @RequestPart(name = "foto2", required = false) MultipartFile archivo) {

	    try {
	        // ✅ Validación básica del nombre
	        if (productor == null || productor.getNombre() == null || productor.getNombre().isBlank()) {
	            return ResponseEntity.badRequest().body(
	                new ErrorMsg(1, "El productor no tiene un nombre válido."));
	        }

	        // ✅ Guardar imagen si se envía
	        if (archivo != null && !archivo.isEmpty()) {
	            String nombreArchivo = Auxiliar.guardarImagen(archivo, rutaFotos);
	            productor.setFoto(nombreArchivo);
	        } else {
	            productor.setFoto("default.png");
	        }

	        // ✅ Insertar en la base de datos
	        boolean insertado = productorServicio.insertarProductor(productor);

	        if (insertado) {
	            return ResponseEntity.ok(new ErrorMsg(0, "Productor insertado correctamente"));
	        } else {
	            return ResponseEntity.internalServerError()
	                .body(new ErrorMsg(1, "No se pudo insertar el productor"));
	        }

	    } catch (IOException e) {
	        return ResponseEntity.internalServerError()
	            .body(new ErrorMsg(1, "Error al guardar la foto: " + e.getMessage()));

	    } catch (Exception e) {
	        return ResponseEntity.internalServerError()
	            .body(new ErrorMsg(2, "Error interno del servidor: " + e.getMessage()));
	    }
	}



	
	@DeleteMapping("/{id}")
	@Operation(summary = "Borrar un productor por su id en la barra de direcciones" )
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Productor obtenido"), 		
		@ApiResponse(responseCode = "404", description = "Productor NO obtenido"), 	
		@ApiResponse(responseCode = "500", description = "Error") 		
	})
	public ResponseEntity<ErrorMsg> borrarUnProductor(@PathVariable String id) {
		try { 
			int idTemp = Integer.parseInt(id.toString());
			
			boolean resultado = productorServicio.borrarProductor(idTemp);
			
			if (resultado) {//200
				return ResponseEntity.ok().body(new ErrorMsg(0, "Productor borrado correctamente"));
			}
			else {//500
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMsg(1, "No se pudo borrar el productor"));
			}
		} catch (Exception e) {//400
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMsg(1, "ID con formato no valido"));
		}
	}
	
	
	@PutMapping()
	@Operation(summary = "Modificar un productor")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Productor modificado", 
			content = @Content(schema = @Schema(implementation =  ErrorMsg.class))), 
		@ApiResponse(responseCode = "500", description = "No se pudo modificar",
			content = @Content(schema = @Schema(implementation =  ErrorMsg.class)))
	})
	public ResponseEntity<ErrorMsg> modificarProductor(@RequestBody Productor productor) {
		boolean modificado = productorServicio.modificarProductor(productor);
		
		if (modificado) {
			return ResponseEntity.ok().body(new ErrorMsg(0, "Productor modificado correctamente"));
		}else {
			return ResponseEntity.internalServerError().body(new ErrorMsg(1, "No se pudo modificar el productor"));
		}
	}

}

