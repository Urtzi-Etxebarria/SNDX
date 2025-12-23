package com.ipartek.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ipartek.modelo.Usuario;

import jakarta.servlet.http.HttpSession;

@Controller
public class InicioControlador {
	
	@RequestMapping("/")
	public String cargarInicio(Model model) {
	
		model.addAttribute("obj_usu", new Usuario(0,"admin","1234","ADMIN"));
		return "login";
	}
	
	@GetMapping("/logout")
	public String cerrarSesion(HttpSession session) {
	    session.invalidate(); 
	    return "redirect:/";
	}
	
}
