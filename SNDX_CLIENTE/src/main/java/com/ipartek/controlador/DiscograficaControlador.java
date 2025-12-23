package com.ipartek.controlador;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ipartek.pojos.Disco;
import com.ipartek.pojos.Discografica;
import com.ipartek.servicios.DiscograficaServicio;
import jakarta.servlet.http.HttpSession;

@Controller
public class DiscograficaControlador {
	
	@Autowired
    private DiscograficaServicio discograficaServicio;
	
	private static final Logger logger = LoggerFactory.getLogger(DiscograficaControlador.class);
	
	@GetMapping("/discograficas")
	public String listarDiscograficas(HttpSession session, Model model) {
	    String token = (String) session.getAttribute("jwt_token");

	    if (token == null) {
	        return "redirect:/";
	    }

	    List<Discografica> listaDiscograficas = discograficaServicio.obtenerTodasDiscograficas(token);

	    for (Discografica discografica : listaDiscograficas) {
	        List<Disco> discografia = discograficaServicio.obtenerDiscografiaPorDiscografica(discografica.getId(), token);
	        discografica.setDiscografia(discografia);
	    }

	    model.addAttribute("listaDiscograficas", listaDiscograficas);

	    return "discograficas";
	}
	
	
	@GetMapping("/discograficas/{id}")
	public String verDiscografica(@PathVariable("id") int id, HttpSession session, Model model) {
	    String token = (String) session.getAttribute("jwt_token");

	    if (token == null) {
	        return "redirect:/";
	    }

	    Discografica discografica = discograficaServicio.obtenerDiscograficaPorId(id, token);
	    model.addAttribute("discografica", discografica);
	    model.addAttribute("listaDiscografia", discograficaServicio.obtenerDiscografiaPorDiscografica(id, token));

	    return "ficha_discografica";
	}
	
	@GetMapping("/administracion/discograficas")
    public String mostrarDiscograficas(HttpSession session, Model model) {
    	String token = (String) session.getAttribute("jwt_token");

	    if (token == null) {
	        return "redirect:/";
	    }

        model.addAttribute("listaDiscograficas", discograficaServicio.obtenerTodasDiscograficas(token));
        model.addAttribute("obj_discografica", new Discografica());

        return "discograficas_crud";
    }
	
	@PostMapping("/GuardarDiscografica")
    public String guardarDiscografica(HttpSession session,
                                 Model model,
                                 @ModelAttribute Discografica obj_discografica,
                                 @RequestParam(name = "foto2", required = false) MultipartFile archivo) {

        String token = (String) session.getAttribute("jwt_token");
        if (token == null) {
            return "redirect:/";
        }

        try {
            // Llamamos al servicio que enviará el artista y la foto al REST
        	discograficaServicio.insertarDiscografica(obj_discografica, archivo, token);

        } catch (IOException e) {
            logger.error("Error al insertar el artista", e);
            model.addAttribute("error", "No se pudo insertar el artista.");
            return "error";
        }

        return "redirect:/discograficas";
    }

}