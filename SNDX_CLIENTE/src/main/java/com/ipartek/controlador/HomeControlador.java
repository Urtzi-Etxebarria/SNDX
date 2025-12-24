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

/**
 * Controlador MVC encargado de gestionar la página principal (home)
 * de la aplicación.
 * <p>
 * Muestra información destacada como:
 * <ul>
 *   <li>Disco de la semana</li>
 *   <li>Artista de la semana</li>
 *   <li>Productor de la semana</li>
 *   <li>Discográfica de la semana</li>
 * </ul>
 * </p>
 * <p>
 * El acceso requiere autenticación mediante un token JWT almacenado
 * en la sesión HTTP.
 * </p>
 */
@Controller
public class HomeControlador {

    /**
     * Servicio encargado de la gestión de discos.
     */
    @Autowired
    private DiscoServicio discoService;

    /**
     * Servicio encargado de la gestión de artistas.
     */
    @Autowired
    private ArtistaServicio artistaService;

    /**
     * Servicio encargado de la gestión de productores.
     */
    @Autowired
    private ProductorServicio productorService;

    /**
     * Servicio encargado de la gestión de discográficas.
     */
    @Autowired
    private DiscograficaServicio discograficaService;

    /**
     * Muestra la página principal de la aplicación.
     * <p>
     * Obtiene elementos destacados de forma aleatoria y los envía
     * a la vista para su visualización.
     * </p>
     *
     * @param session sesión HTTP del usuario
     * @param model modelo para enviar los datos a la vista
     * @return vista principal de la aplicación o redirección al login
     */
    @GetMapping("/home")
    public String home(HttpSession session, Model model) {

        String jwtToken = (String) session.getAttribute("jwt_token");

        if (jwtToken == null) {
            return "redirect:/";
        }

        model.addAttribute("discoSemana", discoService.obtenerDiscoAleatorio(jwtToken));
        model.addAttribute("artistaSemana", artistaService.obtenerArtistaAleatorio(jwtToken));
        model.addAttribute("productorSemana", productorService.obtenerProductorAleatorio(jwtToken));
        model.addAttribute("discograficaSemana", discograficaService.obtenerDiscograficaAleatorio(jwtToken));

        return "home";
    }
}
