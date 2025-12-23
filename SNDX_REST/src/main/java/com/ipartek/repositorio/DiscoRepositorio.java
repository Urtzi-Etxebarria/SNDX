package com.ipartek.repositorio;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ipartek.modelo.Disco;

@Repository
public interface DiscoRepositorio extends JpaRepository<Disco, Integer>{
	
	List<Disco> findByArtistaId(Integer artistaId);
	
	List<Disco> findByProductorId(Integer productorId);
	
	List<Disco> findByDiscograficaId(Integer discograficaId);

	List<Disco> findByGeneroId(Integer generoId);

}
