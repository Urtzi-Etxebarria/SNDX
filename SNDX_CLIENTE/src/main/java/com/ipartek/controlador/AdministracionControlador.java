package com.ipartek.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdministracionControlador {
	
	@GetMapping("/administracion")
    public String inicioAdmin(HttpSession session, Model model) {
		
		return "administracion";
		
	}
}
