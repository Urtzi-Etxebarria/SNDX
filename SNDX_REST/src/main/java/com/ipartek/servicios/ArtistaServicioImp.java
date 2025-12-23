package com.ipartek.servicios;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.ipartek.modelo.Artista;
import com.ipartek.modelo.Disco;
import com.ipartek.repositorio.ArtistaRepositorio;
import com.ipartek.repositorio.DiscoRepositorio;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ArtistaServicioImp implements ArtistaServicio{
	
	@Autowired
	private ArtistaRepositorio artistaRepo;
	
	@Autowired
	private DiscoRepositorio discoRepositorio;
	
	
	@Override
	public List<Artista> obtenerTodosArtistas() {
		return artistaRepo.findAll(Sort.by("nombre").ascending());
	}

	@Override
	public Artista obtenerArtistaPorID(Integer id) {
		int idTemp=0;
		if (id!=null) {
			idTemp=id;
		}
		
		Optional<Artista> artista = artistaRepo.findById(idTemp);
		
		if (artista.isPresent()) {
			return artista.orElse(new Artista());
		}else {
			return new Artista();
		}
	}

	@Override
	public boolean insertarArtista(Artista artista) {
	    // Verificamos que el ID sea 0 (es decir, que es una artista nueva)
	    if (artista.getId() == 0) {
	        
	        // Guardamos la artista en la base de datos
	        Artista artistaTemp = artistaRepo.save(artista);

	        // Comparamos el nombre de la artista guardada con la original
	        if (artistaTemp.getNombre().equals(artista.getNombre())) {
	            return true; // Se insertó correctamente
	        } else {
	            return false; // Algo falló
	        }

	    } else {
	        return false; // Ya tiene ID, no es nueva
	    }
	}


	@Override
	public boolean modificarArtista(Artista artista) {
	    // Buscar la artista en la base de datos usando su ID
	    Optional<Artista> categoriaTemp = artistaRepo.findById(artista.getId());

	    // Si se encontró la artista
	    if (categoriaTemp.isPresent()) {

	        // Se guarda la artista modificada
	    	artistaRepo.save(artista);

	        // Se vuelve a buscar para verificar si se guardó correctamente
	        Optional<Artista> artistaTemp2 = artistaRepo.findById(artista.getId());

	        Artista artista2 = new Artista();

	        // Si se encontró después de guardar
	        if (artistaTemp2.isPresent()) {
	        	artista2 = artistaTemp2.orElse(new Artista());
	        }

	        // Se compara la artista modificada con la recuperada
	        if (!artista.equals(artista2)) {
	            return true; // Se ha modificado correctamente
	        } else {
	            return false; // No se modificó (quizás se envió igual)
	        }

	    } else {
	        // No se encontró la artista, no se puede modificar
	        return false;
	    }
	}

	@Override
	public boolean borrarArtista(Integer id) {
	    // Buscar la artista por ID
	    Optional<Artista> artistaTemp = artistaRepo.findById(id);

	    // Si existe, se elimina
	    if (artistaTemp.isPresent()) {
	    	artistaRepo.deleteById(id);

	        // Verificación opcional: confirmar que ya no existe
	        boolean existe = artistaRepo.findById(id).isPresent();
	        return !existe;
	    }

	    // Si no existe la artista, no se puede eliminar
	    return false;
	}
	
	@Override
	public List<Disco> obtenerDiscografiaPorArtista(Integer artistaId) {
        if (!artistaRepo.existsById(artistaId)) {
            throw new EntityNotFoundException("Artista no encontrado con id: " + artistaId);
        }
        return discoRepositorio.findByArtistaId(artistaId);
    }
	
}