package com.ipartek.servicios;

import java.util.List;
import com.ipartek.modelo.Disco;
import com.ipartek.modelo.Productor;

public interface ProductorServicio {
	
	List<Productor> obtenerTodosProductores();
	
	List<Disco> obtenerDiscografiaPorProductor(Integer productorId);

	Productor obtenerProductorPorID(Integer id);

	boolean insertarProductor(Productor productor);

	boolean borrarProductor(Integer id);

	boolean modificarProductor(Productor productor);

}