package com.ipartek.servicios;

import java.util.List;
import com.ipartek.pojos.Disco;
import com.ipartek.pojos.Genero;

public interface GeneroServicio {
	
	 List<Genero> obtenerTodosGeneros(String jwtToken);

	 List<Disco> obtenerDiscosPorGenero(int idGenero, String jwtToken);

	 Genero obtenerGeneroPorId(int id, String jwtToken);

	 void modificarGenero(Genero genero, String jwtToken);

	 void insertarGenero(Genero genero, String jwtToken);

	 void borrarGenero(Integer id, String jwtToken);

}
