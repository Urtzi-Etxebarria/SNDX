package com.ipartek.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ipartek.modelo.Artista;

@Repository
public interface ArtistaRepositorio extends JpaRepository<Artista, Integer>{
	
	

}
