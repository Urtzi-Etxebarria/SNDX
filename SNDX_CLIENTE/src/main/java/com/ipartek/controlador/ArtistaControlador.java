package com.ipartek.controlador;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.ipartek.pojos.Artista;
import com.ipartek.pojos.Disco;
import com.ipartek.servicios.ArtistaServicio;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
public class ArtistaControlador {
	
	@Value("${ruta.imagenes.artistas}") // Inyecta la ruta de imágenes desde application.properties
	String rutaFotos;
	
    @Autowired
    private ArtistaServicio artistaServicio;
    
    private static final Logger logger = LoggerFactory.getLogger(ArtistaControlador.class);
    
    @GetMapping("/artistas")
    public String listaTodosArtistas(HttpSession session, Model model) {
        String token = (String) session.getAttribute("jwt_token");

        if (token == null) {
            return "redirect:/";
        }

        List<Artista> listaArtistas = artistaServicio.obtenerTodosArtistas(token);

        for (Artista artista : listaArtistas) {
            List<Disco> discografia = artistaServicio.obtenerDiscografiaPorArtista(artista.getId(), token);
            artista.setDiscografia(discografia);
        }

        model.addAttribute("listaArtistas", listaArtistas);

        return "artistas";
    }
    
    @GetMapping("/artistas/{id}")
	public String verArtistaPorId(@PathVariable("id") int id, HttpSession session, Model model) {
	    String token = (String) session.getAttribute("jwt_token");

	    if (token == null) {
	        return "redirect:/";
	    }

	    Artista artista = artistaServicio.obtenerArtistaPorId(id, token);
	    model.addAttribute("artista", artista);

	    return "ficha_artista";
	}
    
    @GetMapping("/administracion/artistas")
    public String mostrarArtistasAdmin(HttpSession session, Model model) {
    	String token = (String) session.getAttribute("jwt_token");

	    if (token == null) {
	        return "redirect:/";
	    }

        model.addAttribute("listaArtistas", artistaServicio.obtenerTodosArtistas(token));
        model.addAttribute("obj_artista", new Artista());

        return "artistas_crud";
    }
    
    @PostMapping("/GuardarArtista")
    public String guardarArtista(HttpSession session,
                                 Model model,
                                 @ModelAttribute Artista obj_artista,
                                 @RequestParam(name = "foto2", required = false) MultipartFile archivo) {

        String token = (String) session.getAttribute("jwt_token");
        if (token == null) {
            return "redirect:/";
        }

        try {
            // Llamamos al servicio que enviará el artista y la foto al REST
            artistaServicio.insertarArtista(obj_artista, archivo, token);

        } catch (IOException e) {
            logger.error("Error al insertar el artista", e);
            model.addAttribute("error", "No se pudo insertar el artista.");
            return "error";
        }

        return "redirect:/artistas";
    }

}