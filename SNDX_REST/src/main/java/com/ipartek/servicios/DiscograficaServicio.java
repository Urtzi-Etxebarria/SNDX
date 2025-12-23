package com.ipartek.servicios;

import java.util.List;
import com.ipartek.modelo.Disco;
import com.ipartek.modelo.Discografica;

public interface DiscograficaServicio {
	
	List<Discografica> obtenerTodasDiscograficas();
	
	List<Disco> obtenerDiscografiaPorDiscografica(Integer discograficaId);

	Discografica obtenerDiscograficaPorID(Integer id);

	boolean insertarDiscografica(Discografica discografica);

	boolean borrarDiscografica(Integer id);

	boolean modificarDiscografica(Discografica discografica);

	

}
