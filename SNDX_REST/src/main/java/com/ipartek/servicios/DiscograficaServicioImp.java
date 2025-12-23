package com.ipartek.servicios;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ipartek.modelo.Disco;
import com.ipartek.modelo.Discografica;
import com.ipartek.repositorio.DiscoRepositorio;
import com.ipartek.repositorio.DiscograficaRepositorio;

import jakarta.persistence.EntityNotFoundException;

@Service
public class DiscograficaServicioImp implements DiscograficaServicio{
	
	@Autowired
	private DiscograficaRepositorio discograficaRepo;
	
	@Autowired
	private DiscoRepositorio discoRepositorio;
	

	@Override
	public List<Discografica> obtenerTodasDiscograficas() {
		return discograficaRepo.findAll(Sort.by("nombre").ascending());
	}

	@Override
	public Discografica obtenerDiscograficaPorID(Integer id) {
		int idTemp=0;
		if (id!=null) {
			idTemp=id;
		}
		
		Optional<Discografica> discografica = discograficaRepo.findById(idTemp);
		
		if (discografica.isPresent()) {
			return discografica.orElse(new Discografica());
		}else {
			return new Discografica();
		}
	}

	@Override
	public boolean insertarDiscografica(Discografica discografica) {
		// Verificamos que el ID sea 0 (es decir, que es una discografica nueva)
	    if (discografica.getId() == 0) {
	        
	        // Guardamos la discografica en la base de datos
	    	Discografica discograficaTemp = discograficaRepo.save(discografica);

	        // Comparamos el nombre de la discografica guardada con la original
	        if (discograficaTemp.getNombre().equals(discografica.getNombre())) {
	            return true; // Se insertó correctamente
	        } else {
	            return false; // Algo falló
	        }

	    } else {
	        return false; // Ya tiene ID, no es nueva
	    }
	}

	@Override
	public boolean borrarDiscografica(Integer id) {
		// Buscar la discografica por ID
	    Optional<Discografica> discograficaTemp = discograficaRepo.findById(id);

	    // Si existe, se elimina
	    if (discograficaTemp.isPresent()) {
	    	discograficaRepo.deleteById(id);

	        // Verificación opcional: confirmar que ya no existe
	        boolean existe = discograficaRepo.findById(id).isPresent();
	        return !existe;
	    }

	    // Si no existe la discografica, no se puede eliminar
	    return false;
	}
		

	@Override
	public boolean modificarDiscografica(Discografica discografica) {
		// Buscar la discografica en la base de datos usando su ID
	    Optional<Discografica> discograficaTemp = discograficaRepo.findById(discografica.getId());

	    // Si se encontró la discografica
	    if (discograficaTemp.isPresent()) {

	        // Se guarda la discografica modificada
	    	discograficaRepo.save(discografica);

	        // Se vuelve a buscar para verificar si se guardó correctamente
	        Optional<Discografica> discograficaTemp2 = discograficaRepo.findById(discografica.getId());

	        Discografica discografica2 = new Discografica();

	        // Si se encontró después de guardar
	        if (discograficaTemp2.isPresent()) {
	        	discografica2 = discograficaTemp2.orElse(new Discografica());
	        }

	        // Se compara la discografica modificada con la recuperada
	        if (!discografica.equals(discografica2)) {
	            return true; // Se ha modificado correctamente
	        } else {
	            return false; // No se modificó (quizás se envió igual)
	        }

	    } else {
	        // No se encontró la discografica, no se puede modificar
	        return false;
	    }
	}
	
	@Override
	public List<Disco> obtenerDiscografiaPorDiscografica(Integer discograficaId) {
        if (!discograficaRepo.existsById(discograficaId)) {
            throw new EntityNotFoundException("Discografica no encontrada con id: " + discograficaId);
        }
        return discoRepositorio.findByDiscograficaId(discograficaId);
    }

}
