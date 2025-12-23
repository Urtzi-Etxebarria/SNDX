package com.ipartek.servicios;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.ipartek.pojos.Disco;
import com.ipartek.pojos.Discografica;

public interface DiscograficaServicio {
	
	List<Discografica> obtenerTodasDiscograficas(String jwtToken);
	
	List<Disco> obtenerDiscografiaPorDiscografica(int idDiscografica, String jwtToken);
	
	Discografica obtenerDiscograficaPorId(int id, String jwtToken);
	
	Discografica obtenerDiscograficaAleatorio(String jwtToken);

	void modificarDiscografica(Discografica obj_discografica, String jwtToken);

	void insertarDiscografica(Discografica obj_discografica, MultipartFile archivo, String jwtToken) throws IOException;

	void borrarDiscografica(Integer id, String jwtToken);

}
