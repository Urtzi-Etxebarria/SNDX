package com.ipartek.servicios;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.ipartek.pojos.Disco;

public interface DiscoServicio {
	
	List<Disco> obtenerTodosDiscos(String jwtToken);
	
	Disco obtenerDiscoPorId(int id, String jwtToken);
	
	Disco obtenerDiscoAleatorio(String jwtToken);

	void modificarDisco(Disco disco, String jwtToken);

	void insertarDisco(Disco disco, MultipartFile archivo, String jwtToken) throws IOException;

	void borrarDisco(Integer id, String jwtToken);

}
