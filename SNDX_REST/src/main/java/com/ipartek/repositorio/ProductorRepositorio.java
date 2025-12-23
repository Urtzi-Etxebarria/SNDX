package com.ipartek.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ipartek.modelo.Productor;

@Repository
public interface ProductorRepositorio extends JpaRepository<Productor, Integer>{

}
