package com.ipartek.auxiliar;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.web.multipart.MultipartFile;

public class Auxiliar {
	
	public static String guardarImagen(MultipartFile archivo, String rutaFotos) throws IOException {
	    LocalDateTime fecha = LocalDateTime.now();
	    DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss-SSS");
	    String semilla = fecha.format(formato);

	    String nombreArchivo = semilla + archivo.getOriginalFilename();
	    Path ruta = Paths.get(rutaFotos, nombreArchivo);

	    // ⚡ No uses try-catch aquí — deja que IOException suba al controlador
	    Files.write(ruta, archivo.getBytes());

	    return nombreArchivo;
	}

	
	public static void borrarFoto(String rutaFotos, String nombreFoto) {
		Path ruta = Paths.get(rutaFotos + nombreFoto);
		
		try {
			if (!nombreFoto.equals("default.jpg")) {
				Files.deleteIfExists(ruta);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}		
	
}