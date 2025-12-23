package com.ipartek.servicios;

import java.util.List;
import com.ipartek.modelo.Disco;

public interface DiscoServicio {
	
	List<Disco> obtenerTodosDiscos();

	Disco obtenerDiscoPorID(Integer id);

	boolean insertarDisco(Disco disco);

	boolean borrarDisco(Integer id);

	boolean modificarDisco(Disco disco);

}
