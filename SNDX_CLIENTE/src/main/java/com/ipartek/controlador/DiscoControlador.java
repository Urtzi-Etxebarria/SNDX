package com.ipartek.controlador;

import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.ipartek.pojos.Disco;
import com.ipartek.servicios.ArtistaServicio;
import com.ipartek.servicios.DiscoServicio;
import com.ipartek.servicios.DiscograficaServicio;
import com.ipartek.servicios.GeneroServicio;
import com.ipartek.servicios.ProductorServicio;
import jakarta.servlet.http.HttpSession;

@Controller
public class DiscoControlador {
	
	@Value("${ruta.imagenes.discos}") // Inyecta la ruta de imágenes desde application.properties
	String rutaFotos;
	
	private static final Logger logger = LoggerFactory.getLogger(DiscoControlador.class);
	
	@Autowired
    private DiscoServicio discoServicio;
	
	@Autowired
    private ArtistaServicio artistaServicio;
	
	@Autowired
    private DiscograficaServicio discograficaServicio;
	
	@Autowired
    private ProductorServicio productorServicio;
	
	@Autowired
    private GeneroServicio generoServicio;
	
	@GetMapping("/discos")
	public String listarDiscos(HttpSession session, Model model) {
        String token = (String) session.getAttribute("jwt_token");

        if (token == null) {
            return "redirect:/";
        }

        List<Disco> listaDiscos = discoServicio.obtenerTodosDiscos(token);
        model.addAttribute("listaDiscos", listaDiscos);

        return "discos";
    }
	
	@GetMapping("/discos/{id}")
	public String verDisco(@PathVariable("id") int id, HttpSession session, Model model) {
	    String token = (String) session.getAttribute("jwt_token");

	    if (token == null) {
	        return "redirect:/";
	    }

	    Disco disco = discoServicio.obtenerDiscoPorId(id, token);
	    model.addAttribute("disco", disco);

	    return "ficha_disco";
	}
	
	@GetMapping("/administracion/discos")
    public String mostrarDiscos(HttpSession session, Model model) {
    	String token = (String) session.getAttribute("jwt_token");

	    if (token == null) {
	        return "redirect:/";
	    }

        model.addAttribute("listaDiscos", discoServicio.obtenerTodosDiscos(token));
        model.addAttribute("obj_disco", new Disco());
        model.addAttribute("listaArtistas", artistaServicio.obtenerTodosArtistas(token));
	    model.addAttribute("listaDiscograficas", discograficaServicio.obtenerTodasDiscograficas(token));
	    model.addAttribute("listaGeneros", generoServicio.obtenerTodosGeneros(token));
	    model.addAttribute("listaProductores", productorServicio.obtenerTodosProductores(token));

        return "discos_crud";
    }
	
	@PostMapping("/GuardarDisco")
	public String guardarDisco(HttpSession session,
	                           Model model,
	                           @RequestParam("nombre") String nombre,
	                           @RequestParam("puntuacion") int puntuacion,
	                           @RequestParam("fecha") String fecha,
	                           @RequestParam("enlaceWikipedia") String enlaceWikipedia,
	                           @RequestParam("enlaceSpotify") String enlaceSpotify,
	                           @RequestParam("enlaceTidal") String enlaceTidal,
	                           @RequestParam("artista") int artistaId,
	                           @RequestParam("discografica") int discograficaId,
	                           @RequestParam("genero") int generoId,
	                           @RequestParam("productor") int productorId,
	                           @RequestParam(name = "foto2", required = false) MultipartFile archivo) {

	    String token = (String) session.getAttribute("jwt_token");
	    if (token == null) return "redirect:/";

	    Disco obj_disco = new Disco();
	    obj_disco.setNombre(nombre);
	    obj_disco.setPuntuacion(puntuacion);
	    obj_disco.setFecha(fecha);
	    obj_disco.setEnlaceWikipedia(enlaceWikipedia);
	    obj_disco.setEnlaceSpotify(enlaceSpotify);
	    obj_disco.setEnlaceTidal(enlaceTidal);

	    obj_disco.setArtista(artistaServicio.obtenerArtistaPorId(artistaId, token));
	    obj_disco.setDiscografica(discograficaServicio.obtenerDiscograficaPorId(discograficaId, token));
	    obj_disco.setGenero(generoServicio.obtenerGeneroPorId(generoId, token));
	    obj_disco.setProductor(productorServicio.obtenerProductorPorId(productorId, token));

	    try {
	    	
	        discoServicio.insertarDisco(obj_disco, archivo, token);

	        
	    } catch (IOException e) {
	    	
	        logger.error("Error al insertar el disco", e);
	        model.addAttribute("error", "No se pudo insertar el disco.");
	        return "error";
	        
	    }

	    return "redirect:/discos";
	}

}
