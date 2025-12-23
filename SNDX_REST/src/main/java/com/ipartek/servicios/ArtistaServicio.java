package com.ipartek.servicios;

import java.util.List;
import com.ipartek.modelo.Artista;
import com.ipartek.modelo.Disco;

public interface ArtistaServicio {

	List<Artista> obtenerTodosArtistas();
	
	List<Disco> obtenerDiscografiaPorArtista(Integer artistaId);

	Artista obtenerArtistaPorID(Integer id);

	boolean insertarArtista(Artista artista);

	boolean borrarArtista(Integer id);

	boolean modificarArtista(Artista artista);

}
