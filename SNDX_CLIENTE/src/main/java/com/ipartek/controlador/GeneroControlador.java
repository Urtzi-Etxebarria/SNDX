package com.ipartek.controlador;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.ipartek.pojos.Disco;
import com.ipartek.pojos.Genero;
import com.ipartek.servicios.GeneroServicio;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

@Controller
public class GeneroControlador {
	
    @Autowired
    private GeneroServicio generoServicio;

    @GetMapping("/generos")
    public String listarGeneros(HttpSession session, Model model) {
        String token = (String) session.getAttribute("jwt_token");

        if (token == null) {
            return "redirect:/";
        }

        List<Genero> listaGeneros = generoServicio.obtenerTodosGeneros(token);

        // Si quieres cargar discos en cada género (opcional)
        for (Genero genero : listaGeneros) {
            List<Disco> discos = generoServicio.obtenerDiscosPorGenero(genero.getId(), token);
            genero.setDiscos(discos);  // Asegúrate que Genero tenga setDiscos()
        }

        model.addAttribute("listaGeneros", listaGeneros);

        return "generos";  // nombre de la vista para listar géneros
    }
    
    @GetMapping("/generos/{id}")
	public String verGenero(@PathVariable("id") int id, HttpSession session, Model model) {
	    String token = (String) session.getAttribute("jwt_token");

	    if (token == null) {
	        return "redirect:/";
	    }

	    Genero genero = generoServicio.obtenerGeneroPorId(id, token);
	    model.addAttribute("genero", genero);

	    return "generos";  // nombre de la vista para ver detalle de género
	}
    
    @GetMapping("/administracion/generos")
    public String mostrarGeneros(HttpSession session, Model model) {
    	String token = (String) session.getAttribute("jwt_token");

	    if (token == null) {
	        return "redirect:/";
	    }

        model.addAttribute("listaGeneros", generoServicio.obtenerTodosGeneros(token));
        model.addAttribute("obj_genero", new Genero());

        return "genero_crud";
    }
}

