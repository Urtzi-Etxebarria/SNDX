package com.ipartek.servicios;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ipartek.modelo.Disco;
import com.ipartek.modelo.Productor;
import com.ipartek.repositorio.DiscoRepositorio;
import com.ipartek.repositorio.ProductorRepositorio;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductorServicioImp implements ProductorServicio{
	
	@Autowired
	private ProductorRepositorio productorRepo;
	
	@Autowired
	private DiscoRepositorio discoRepositorio;
	

	@Override
	public List<Productor> obtenerTodosProductores() {
		return productorRepo.findAll(Sort.by("nombre").ascending());
	}

	@Override
	public Productor obtenerProductorPorID(Integer id) {
		int idTemp=0;
		if (id!=null) {
			idTemp=id;
		}
		
		Optional<Productor> productor = productorRepo.findById(idTemp);
		
		if (productor.isPresent()) {
			return productor.orElse(new Productor());
		}else {
			return new Productor();
		}
	}

	@Override
	public boolean insertarProductor(Productor productor) {
		// Verificamos que el ID sea 0 (es decir, que es un productor nuevo)
	    if (productor.getId() == 0) {
	        
	        // Guardamos el productor en la base de datos
	    	Productor productorTemp = productorRepo.save(productor);

	        // Comparamos el nombre del productor guardado con la original
	        if (productorTemp.getNombre().equals(productor.getNombre())) {
	            return true; // Se insertó correctamente
	        } else {
	            return false; // Algo falló
	        }

	    } else {
	        return false; // Ya tiene ID, no es nueva
	    }
	}

	@Override
	public boolean borrarProductor(Integer id) {
		// Buscar el disco por ID
	    Optional<Productor> productorTemp = productorRepo.findById(id);

	    // Si existe, se elimina
	    if (productorTemp.isPresent()) {
	    	productorRepo.deleteById(id);

	        // Verificación opcional: confirmar que ya no existe
	        boolean existe = productorRepo.findById(id).isPresent();
	        return !existe;
	    }

	    // Si no existe el disco, no se puede eliminar
	    return false;
	}

	@Override
	public boolean modificarProductor(Productor productor) {
		// Buscar el productor en la base de datos usando su ID
	    Optional<Productor> productorTemp = productorRepo.findById(productor.getId());

	    // Si se encontró el productor
	    if (productorTemp.isPresent()) {

	        // Se guarda el productor modificado
	    	productorRepo.save(productor);

	        // Se vuelve a buscar para verificar si se guardó correctamente
	        Optional<Productor> productorTemp2 = productorRepo.findById(productor.getId());

	        Productor productor2 = new Productor();

	        // Si se encontró después de guardar
	        if (productorTemp2.isPresent()) {
	        	productor2 = productorTemp2.orElse(new Productor());
	        }

	        // Se compara el productor modificado con el recuperado
	        if (!productor.equals(productor2)) {
	            return true; // Se ha modificado correctamente
	        } else {
	            return false; // No se modificó (quizás se envió igual)
	        }

	    } else {
	        // No se encontró el productor, no se puede modificar
	        return false;
	    }
	}
	
	@Override
	public List<Disco> obtenerDiscografiaPorProductor(Integer productorId) {
        if (!productorRepo.existsById(productorId)) {
            throw new EntityNotFoundException("Productor no encontrado con id: " + productorId);
        }
        return discoRepositorio.findByProductorId(productorId);
    }

}
