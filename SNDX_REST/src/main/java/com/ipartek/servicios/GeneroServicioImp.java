package com.ipartek.servicios;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ipartek.modelo.Disco;
import com.ipartek.modelo.Genero;
import com.ipartek.repositorio.DiscoRepositorio;
import com.ipartek.repositorio.GeneroRepositorio;

import jakarta.persistence.EntityNotFoundException;

@Service
public class GeneroServicioImp implements GeneroServicio{
	
	@Autowired
	private GeneroRepositorio generoRepo;
	
	@Autowired
	private DiscoRepositorio discoRepo;

	@Override
	public List<Genero> obtenerTodosGeneros() {
		return generoRepo.findAll(Sort.by("nombre").ascending());
	}

	@Override
	public Genero obtenerGeneroPorID(Integer id) {
		int idTemp=0;
		if (id!=null) {
			idTemp=id;
		}
		
		Optional<Genero> genero = generoRepo.findById(idTemp);
		
		if (genero.isPresent()) {
			return genero.orElse(new Genero());
		}else {
			return new Genero();
		}
	}

	@Override
	public boolean insertarGenero(Genero genero) {
		// Verificamos que el ID sea 0 (es decir, que es un genero nuevo)
	    if (genero.getId() == 0) {
	        
	        // Guardamos el genero en la base de datos
	    	Genero generoTemp = generoRepo.save(genero);

	        // Comparamos el nombre del genero guardado con la original
	        if (generoTemp.getNombre().equals(genero.getNombre())) {
	            return true; // Se insertó correctamente
	        } else {
	            return false; // Algo falló
	        }

	    } else {
	        return false; // Ya tiene ID, no es nueva
	    }
	}

	@Override
	public boolean borrarGenero(Integer id) {
		// Buscar el genero por ID
	    Optional<Genero> generoTemp = generoRepo.findById(id);

	    // Si existe, se elimina
	    if (generoTemp.isPresent()) {
	    	generoRepo.deleteById(id);

	        // Verificación opcional: confirmar que ya no existe
	        boolean existe = generoRepo.findById(id).isPresent();
	        return !existe;
	    }

	    // Si no existe el genero, no se puede eliminar
	    return false;
	}

	@Override
	public boolean modificarGenero(Genero genero) {
		// Buscar el disco en la base de datos usando su ID
	    Optional<Genero> generoTemp = generoRepo.findById(genero.getId());

	    // Si se encontró el disco
	    if (generoTemp.isPresent()) {

	        // Se guarda el disco modificado
	    	generoRepo.save(genero);

	        // Se vuelve a buscar para verificar si se guardó correctamente
	        Optional<Genero> generoTemp2 = generoRepo.findById(genero.getId());

	        Genero genero2 = new Genero();

	        // Si se encontró después de guardar
	        if (generoTemp2.isPresent()) {
	        	genero2 = generoTemp2.orElse(new Genero());
	        }

	        // Se compara el disco modificado con la recuperado
	        if (!genero.equals(genero2)) {
	            return true; // Se ha modificado correctamente
	        } else {
	            return false; // No se modificó (quizás se envió igual)
	        }

	    } else {
	        // No se encontró el disco, no se puede modificar
	        return false;
	    }
	}
	
	@Override
	public List<Disco> obtenerDiscografiaPorGenero(Integer generoId) {
        if (!generoRepo.existsById(generoId)) {
            throw new EntityNotFoundException("Género no encontrado con id: " + generoId);
        }
        return discoRepo.findByGeneroId(generoId);
    }
	
}
