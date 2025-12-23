package com.ipartek.servicios;

import java.util.List;
import com.ipartek.modelo.Disco;
import com.ipartek.modelo.Genero;

public interface GeneroServicio {
	
	List<Genero> obtenerTodosGeneros();
	
	List<Disco> obtenerDiscografiaPorGenero(Integer generoId);

	Genero obtenerGeneroPorID(Integer id);

	boolean insertarGenero(Genero genero);

	boolean borrarGenero(Integer id);

	boolean modificarGenero(Genero genero);

}
