package com.ipartek.controlador;

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
import com.ipartek.modelo.Artista;
import com.ipartek.modelo.Disco;
import com.ipartek.pojos.ErrorMsg;
import com.ipartek.servicios.DiscoServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/discos")
@Tag(name = "Discos", description = "Operaciones relacionadas con los discos")
public class DiscoControladorREST {
	
	@Value("${ruta.imagenes.artistas}") // Inyecta la ruta de imágenes desde application.properties
	String rutaFotos;
	
	@Autowired
	private DiscoServicio discoServicio;
	
	@GetMapping("")
	@Operation(summary = "Obtener todos los discos")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Discos obtenidos", 
			content = @Content(array = @ArraySchema(schema = @Schema(implementation = Artista.class)))), 
		@ApiResponse(responseCode = "404", description = "Disco está vacío",
			content = @Content(schema = @Schema(implementation =  ErrorMsg.class)))
	})
	public ResponseEntity<Object> obtenerTodosDiscos() {
		
		List<Disco> disco = discoServicio.obtenerTodosDiscos();

		if (disco.size()!=0) {//200
			return ResponseEntity.ok().body(disco);
		}
		else {//404
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMsg(1, "No se pudo encontar el disco"));
		}
	}

	
	@GetMapping("/{id}")
	@Operation(summary = "Obtener un disco por su id en la barra de direcciones")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Disco obtenido", 
			content = @Content(schema = @Schema(implementation =  Artista.class))), 
		@ApiResponse(responseCode = "404", description = "Disco NO obtenido", 
			content = @Content(schema = @Schema(implementation =  ErrorMsg.class))),
		@ApiResponse(responseCode = "400", description = "Parametro mal puesto", 
			content = @Content(schema = @Schema(implementation =  ErrorMsg.class)))
	})
	public ResponseEntity<Object> obtenerDiscoPorId(@PathVariable String id) {
		try { 
			int idTemp = Integer.parseInt(id.toString());
			
			Disco disco = discoServicio.obtenerDiscoPorID(idTemp);
			
			if (disco.getId()!=0) {//200
				return ResponseEntity.ok().body(disco);
			}
			else {//404
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMsg(1, "No se pudo encontar el disco"));
			}
		} catch (Exception e) {//400
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMsg(1, "ID con formato no valido"));
		}
	}
	
	
    @PostMapping
    @Operation(summary = "Insertar un disco")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Disco insertado correctamente",
                     content = @Content(schema = @Schema(implementation = ErrorMsg.class))),
        @ApiResponse(responseCode = "500", description = "No se pudo insertar el disco",
                     content = @Content(schema = @Schema(implementation = ErrorMsg.class)))
    })
    public ResponseEntity<ErrorMsg> insertarDisco(
            @RequestPart("disco") Disco disco,
            @RequestPart(name = "foto2", required = false) MultipartFile archivo) {

        try {
            // Guardar archivo si existe
            if (archivo != null && !archivo.isEmpty()) {
                String nombreArchivo = archivo.getOriginalFilename();
                disco.setFoto(nombreArchivo);
            } else {
                disco.setFoto("default.png");
            }

            boolean insertado = discoServicio.insertarDisco(disco);

            if (insertado) {
                return ResponseEntity.ok(new ErrorMsg(0, "Disco insertado correctamente"));
            } else {
                return ResponseEntity.status(500).body(new ErrorMsg(1, "No se pudo insertar el disco"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorMsg(1, "Error al procesar disco"));
        }
    }

    
	@DeleteMapping("/{id}")
	@Operation(summary = "Borrar un disco por su id en la barra de direcciones" )
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Disco obtenido"), 		
		@ApiResponse(responseCode = "404", description = "Disco NO obtenido"), 	
		@ApiResponse(responseCode = "500", description = "Error") 		
	})
	public ResponseEntity<ErrorMsg> borrarUnDisco(@PathVariable String id) {
		try { 
			int idTemp = Integer.parseInt(id.toString());
			
			boolean resultado = discoServicio.borrarDisco(idTemp);
			
			if (resultado) {//200
				return ResponseEntity.ok().body(new ErrorMsg(0, "Disco borrado correctamente"));
			}
			else {//500
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMsg(1, "No se pudo borrar el disco"));
			}
		} catch (Exception e) {//400
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMsg(1, "ID con formato no valido"));
		}
	}
	
	
	@PutMapping()
	@Operation(summary = "Modificar un disco")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Disco modificado", 
			content = @Content(schema = @Schema(implementation =  ErrorMsg.class))), 
		@ApiResponse(responseCode = "500", description = "No se pudo modificar",
			content = @Content(schema = @Schema(implementation =  ErrorMsg.class)))
	})
	public ResponseEntity<ErrorMsg> modificarArtista(@RequestBody Disco disco) {
		boolean modificado = discoServicio.modificarDisco(disco);;
		
		if (modificado) {
			return ResponseEntity.ok().body(new ErrorMsg(0, "Disco modificada correctamente"));
		}else {
			return ResponseEntity.internalServerError().body(new ErrorMsg(1, "No se pudo modificar el disco"));
		}
	}

}
