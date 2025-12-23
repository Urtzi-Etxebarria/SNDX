package com.ipartek.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ipartek.modelo.Discografica;

@Repository
public interface DiscograficaRepositorio extends JpaRepository<Discografica, Integer>{

}
