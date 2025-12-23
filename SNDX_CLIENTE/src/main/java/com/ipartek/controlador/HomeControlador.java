package com.ipartek.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.ipartek.servicios.ArtistaServicio;
import com.ipartek.servicios.DiscoServicio;
import com.ipartek.servicios.DiscograficaServicio;
import com.ipartek.servicios.ProductorServicio;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeControlador {
	
	@Autowired
    private DiscoServicio discoService;
    @Autowired
    private ArtistaServicio artistaService;
    @Autowired
    private ProductorServicio productorService;
    @Autowired
    private DiscograficaServicio discograficaService;

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        // 1️⃣ Obtener el token JWT de la sesión
        String jwtToken = (String) session.getAttribute("jwt_token");

        // 2️⃣ Si el token no existe, redirigir al login
        if (jwtToken == null) {
            return "redirect:/";
        }

        // 3️⃣ Llamar a los servicios usando el token
        model.addAttribute("discoSemana", discoService.obtenerDiscoAleatorio(jwtToken));
        model.addAttribute("artistaSemana", artistaService.obtenerArtistaAleatorio(jwtToken));
        model.addAttribute("productorSemana", productorService.obtenerProductorAleatorio(jwtToken));
        model.addAttribute("discograficaSemana", discograficaService.obtenerDiscograficaAleatorio(jwtToken));

        // 4️⃣ Retornar la vista del home
        return "home";
    }

}
