package com.ipartek.servicios;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.ipartek.pojos.Artista;
import com.ipartek.pojos.Disco;

public interface ArtistaServicio {
	
	List<Artista> obtenerTodosArtistas(String jwtToken);
	
	List<Disco> obtenerDiscografiaPorArtista(int id, String jwtToken);
	
	Artista obtenerArtistaPorId(int id, String jwtToken);

	void modificarArtista(Artista obj_artista, String jwtToken);

	void insertarArtista(Artista obj_artista, MultipartFile archivo, String jwtToken) throws IOException;

	void borrarArtista(Integer id, String jwtToken);

	Artista obtenerArtistaAleatorio(String jwtToken);

	

}
