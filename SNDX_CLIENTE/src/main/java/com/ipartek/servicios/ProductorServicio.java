package com.ipartek.servicios;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.ipartek.pojos.Disco;
import com.ipartek.pojos.Productor;

public interface ProductorServicio {
	
	List<Productor> obtenerTodosProductores(String jwtToken);
	
	List<Disco> obtenerDiscografiaPorProductor(int idProductor, String jwtToken);
	
	Productor obtenerProductorPorId(int id, String jwtToken);
	
	Productor obtenerProductorAleatorio(String jwtToken);

	void modificarProductor(Productor obj_productor, String jwtToken);

	void insertarProductor(Productor obj_productor, MultipartFile archivo, String jwtToken) throws IOException;

	void borrarProductor(Integer id, String jwtToken);

	

}
