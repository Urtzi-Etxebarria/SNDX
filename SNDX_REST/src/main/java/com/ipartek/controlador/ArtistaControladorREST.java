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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ipartek.auxiliar.Auxiliar;
import com.ipartek.modelo.Artista;
import com.ipartek.modelo.Disco;
import com.ipartek.pojos.ErrorMsg;
import com.ipartek.servicios.ArtistaServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/artistas")
@Tag(name = "Artistas", description = "Operaciones relacionadas con los artistas")
public class ArtistaControladorREST {
	
	@Value("${ruta.imagenes.artistas}") // Inyecta la ruta de imágenes desde application.properties
	String rutaFotos;
	
	@Autowired
	private ArtistaServicio artistaServicio;
	
	@GetMapping("")
	@Operation(summary = "Obtener todos los artistas")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Artistas obtenidos", 
			content = @Content(array = @ArraySchema(schema = @Schema(implementation = Artista.class)))), 
		@ApiResponse(responseCode = "404", description = "Artista está vacío",
			content = @Content(schema = @Schema(implementation =  ErrorMsg.class)))
	})
	public ResponseEntity<Object> obtenerTodosArtistas() {
		
		List<Artista> artista = artistaServicio.obtenerTodosArtistas();

		if (artista.size()!=0) {//200
			return ResponseEntity.ok().body(artista);
		}
		else {//404
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMsg(1, "No se pudo encontar el artista"));
		}
	}

	
	@GetMapping("/{id}")
	@Operation(summary = "Obtener un artista por su id en la barra de direcciones")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Artista obtenido", 
			content = @Content(schema = @Schema(implementation =  Artista.class))), 
		@ApiResponse(responseCode = "404", description = "Artista NO obtenido", 
			content = @Content(schema = @Schema(implementation =  ErrorMsg.class))),
		@ApiResponse(responseCode = "400", description = "Parametro mal puesto", 
			content = @Content(schema = @Schema(implementation =  ErrorMsg.class)))
	})
	public ResponseEntity<Object> obtenerArtistaPorId(@PathVariable String id) {
		try { 
			int idTemp = Integer.parseInt(id.toString());
			
			Artista artista = artistaServicio.obtenerArtistaPorID(idTemp);
			
			if (artista.getId()!=0) {//200
				return ResponseEntity.ok().body(artista);
			}
			else {//404
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMsg(1, "No se pudo encontar el artista"));
			}
		} catch (Exception e) {//400
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMsg(1, "ID con formato no valido"));
		}
	}
	
	
	@GetMapping("/{id}/discografia")
	@Operation(summary = "Obtener discografía de un artista por su ID")
	@ApiResponses({
	    @ApiResponse(responseCode = "200", description = "Discografía obtenida correctamente",
	        content = @Content(array = @ArraySchema(schema = @Schema(implementation = Disco.class)))),
	    @ApiResponse(responseCode = "404", description = "Artista no encontrado o sin discografía",
	        content = @Content(schema = @Schema(implementation = ErrorMsg.class))),
	    @ApiResponse(responseCode = "400", description = "Parámetro ID mal formado",
	        content = @Content(schema = @Schema(implementation = ErrorMsg.class)))
	})
	public ResponseEntity<Object> obtenerDiscografiaPorArtista(@PathVariable String id) {
	    try {
	        int idInt = Integer.parseInt(id);

	        List<Disco> discografia = artistaServicio.obtenerDiscografiaPorArtista(idInt);

	        if (discografia != null && !discografia.isEmpty()) {
	            return ResponseEntity.ok(discografia); // 200
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(new ErrorMsg(1, "El artista no tiene discos o no existe.")); // 404
	        }

	    } catch (NumberFormatException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	            .body(new ErrorMsg(1, "El ID del artista no tiene un formato válido.")); // 400
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body(new ErrorMsg(2, "Error interno del servidor: " + e.getMessage()));
	    }
	}

	
	@PostMapping()
	@Operation(summary = "Insertar un artista")
	@ApiResponses({
	    @ApiResponse(responseCode = "200", description = "Artista guardado"), 
	    @ApiResponse(responseCode = "500", description = "Artista NO guardado")
	})
	public ResponseEntity<ErrorMsg> insertarArtista(
	        @RequestPart("artista") Artista artista,
	        @RequestPart(name = "foto2", required = false) MultipartFile archivo) {

	    try {
	        // Guardar la imagen si se ha enviado
	        if (archivo != null && !archivo.isEmpty()) {
	            String nombreArchivo = Auxiliar.guardarImagen(archivo, rutaFotos);
	            artista.setFoto(nombreArchivo);
	        } else {
	            artista.setFoto("default.png");
	        }

	        boolean insertado = artistaServicio.insertarArtista(artista);

	        if (insertado) {
	            return ResponseEntity.ok(new ErrorMsg(0, "Artista insertado correctamente"));
	        } else {
	            return ResponseEntity.internalServerError().body(new ErrorMsg(1, "No se pudo insertar el artista"));
	        }

	    } catch (IOException e) {
	        return ResponseEntity.internalServerError().body(new ErrorMsg(1, "Error al guardar la imagen"));
	    }
	}


	@DeleteMapping("/{id}")
	@Operation(summary = "Borrar un artista por su id en la barra de direcciones" )
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Artista obtenido"), 		
		@ApiResponse(responseCode = "404", description = "Artista NO obtenido"), 	
		@ApiResponse(responseCode = "500", description = "Error") 		
	})
	public ResponseEntity<ErrorMsg> borrarUnArtista(@PathVariable String id) {
		try { 
			int idTemp = Integer.parseInt(id.toString());
			
			boolean resultado = artistaServicio.borrarArtista(idTemp);
			
			if (resultado) {//200
				return ResponseEntity.ok().body(new ErrorMsg(0, "Artista borrado correctamente"));
			}
			else {//500
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMsg(1, "No se pudo borrar el artista"));
			}
		} catch (Exception e) {//400
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMsg(1, "ID con formato no valido"));
		}
	}
	
	
	@PutMapping()
	@Operation(summary = "Modificar un artista")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Artista modificado", 
			content = @Content(schema = @Schema(implementation =  ErrorMsg.class))), 
		@ApiResponse(responseCode = "500", description = "No se pudo modificar",
			content = @Content(schema = @Schema(implementation =  ErrorMsg.class)))
	})
	public ResponseEntity<ErrorMsg> modificarArtista(@RequestBody Artista artista) {
		boolean modificado = artistaServicio.modificarArtista(artista);;
		
		if (modificado) {
			return ResponseEntity.ok().body(new ErrorMsg(0, "Artista modificada correctamente"));
		}else {
			return ResponseEntity.internalServerError().body(new ErrorMsg(1, "No se pudo modificar la artista"));
		}
	}

}
