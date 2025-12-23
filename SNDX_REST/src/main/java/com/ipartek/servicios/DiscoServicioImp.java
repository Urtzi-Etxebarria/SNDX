package com.ipartek.servicios;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ipartek.modelo.Disco;
import com.ipartek.repositorio.DiscoRepositorio;
import org.springframework.data.domain.Sort;

@Service
public class DiscoServicioImp implements DiscoServicio{
	
	@Autowired
	private DiscoRepositorio discoRepo;

	@Override
	public List<Disco> obtenerTodosDiscos() {
		return discoRepo.findAll(Sort.by("nombre").ascending());
	}

	@Override
	public Disco obtenerDiscoPorID(Integer id) {
		int idTemp=0;
		if (id!=null) {
			idTemp=id;
		}
		
		Optional<Disco> disco = discoRepo.findById(idTemp);
		
		if (disco.isPresent()) {
			return disco.orElse(new Disco());
		}else {
			return new Disco();
		}
	}

	@Override
	public boolean insertarDisco(Disco disco) {
		// Verificamos que el ID sea 0 (es decir, que es un disco nuevo)
	    if (disco.getId() == 0) {
	        
	        // Guardamos el disco en la base de datos
	    	Disco discoTemp = discoRepo.save(disco);

	        // Comparamos el nombre del disco guardado con la original
	        if (discoTemp.getNombre().equals(disco.getNombre())) {
	            return true; // Se insertó correctamente
	        } else {
	            return false; // Algo falló
	        }

	    } else {
	        return false; // Ya tiene ID, no es nueva
	    }
	}

	@Override
	public boolean borrarDisco(Integer id) {
		// Buscar el disco por ID
	    Optional<Disco> discoTemp = discoRepo.findById(id);

	    // Si existe, se elimina
	    if (discoTemp.isPresent()) {
	    	discoRepo.deleteById(id);

	        // Verificación opcional: confirmar que ya no existe
	        boolean existe = discoRepo.findById(id).isPresent();
	        return !existe;
	    }

	    // Si no existe el disco, no se puede eliminar
	    return false;
	}

	@Override
	public boolean modificarDisco(Disco disco) {
		// Buscar el disco en la base de datos usando su ID
	    Optional<Disco> discoTemp = discoRepo.findById(disco.getId());

	    // Si se encontró el disco
	    if (discoTemp.isPresent()) {

	        // Se guarda el disco modificado
	    	discoRepo.save(disco);

	        // Se vuelve a buscar para verificar si se guardó correctamente
	        Optional<Disco> discoTemp2 = discoRepo.findById(disco.getId());

	        Disco disco2 = new Disco();

	        // Si se encontró después de guardar
	        if (discoTemp2.isPresent()) {
	        	disco2 = discoTemp2.orElse(new Disco());
	        }

	        // Se compara el disco modificado con la recuperado
	        if (!disco.equals(disco2)) {
	            return true; // Se ha modificado correctamente
	        } else {
	            return false; // No se modificó (quizás se envió igual)
	        }

	    } else {
	        // No se encontró el disco, no se puede modificar
	        return false;
	    }
	}

}
