package com.ipartek.controlador;

import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.ipartek.pojos.Disco;
import com.ipartek.pojos.Productor;
import com.ipartek.servicios.ProductorServicio;
import jakarta.servlet.http.HttpSession;

@Controller
public class ProductorControlador {
	
	 @Autowired
	 private ProductorServicio productorServicio;
	 
	 private static final Logger logger = LoggerFactory.getLogger(ProductorControlador.class);

	 @GetMapping("/productores")
	 public String listarProductores(HttpSession session, Model model) {
	     String token = (String) session.getAttribute("jwt_token");

	     if (token == null) {
	         return "redirect:/";
	     }

	     List<Productor> listaProductores = productorServicio.obtenerTodosProductores(token);

	     for (Productor productor : listaProductores) {
	         List<Disco> discografia = productorServicio.obtenerDiscografiaPorProductor(productor.getId(), token);
	         productor.setDiscografia(discografia);
	     }

	     model.addAttribute("listaProductores", listaProductores);

	     return "productores";
	 }
	 
	 
	 @GetMapping("/productores/{id}")
	 public String verProductor(@PathVariable("id") int id, HttpSession session, Model model) {
	    String token = (String) session.getAttribute("jwt_token");

	    if (token == null) {
	        return "redirect:/";
	    }

	    Productor productor = productorServicio.obtenerProductorPorId(id, token);
	    model.addAttribute("productor", productor);
	    model.addAttribute("listaDiscografia", productorServicio.obtenerDiscografiaPorProductor(id, token));

	    return "ficha_productor";
	 }
	 
	 @GetMapping("/administracion/productores")
	    public String mostrarProductores(HttpSession session, Model model) {
	    	String token = (String) session.getAttribute("jwt_token");

		    if (token == null) {
		        return "redirect:/";
		    }

	        model.addAttribute("listaProductores", productorServicio.obtenerTodosProductores(token));
	        model.addAttribute("obj_productor", new Productor());

	        return "productor_crud";
	    }
	 
	 @PostMapping("/GuardarProductor")
	    public String guardarDiscografica(HttpSession session,
	                                 Model model,
	                                 @ModelAttribute Productor obj_productor,
	                                 @RequestParam(name = "foto2", required = false) MultipartFile archivo) {

	        String token = (String) session.getAttribute("jwt_token");
	        if (token == null) {
	            return "redirect:/";
	        }

	        try {
	            // Llamamos al servicio que enviará el artista y la foto al REST
	        	productorServicio.insertarProductor(obj_productor, archivo, token);

	        } catch (IOException e) {
	            logger.error("Error al insertar el artista", e);
	            model.addAttribute("error", "No se pudo insertar el artista.");
	            return "error";
	        }

	        return "redirect:/productores";
	    }

}