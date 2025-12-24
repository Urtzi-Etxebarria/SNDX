package com.ipartek.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ipartek.modelo.Usuario;
import jakarta.servlet.http.HttpSession;

/**
 * Controlador encargado de gestionar la pantalla de inicio de sesión
 * y el cierre de sesión de la aplicación.
 * <p>
 * Proporciona:
 * <ul>
 *   <li>La vista de login</li>
 *   <li>La inicialización del formulario de usuario</li>
 *   <li>La invalidación de la sesión al cerrar sesión</li>
 * </ul>
 * </p>
 */
@Controller
public class InicioControlador {

    /**
     * Carga la página de inicio de sesión.
     * <p>
     * Se inicializa un objeto {@link Usuario} para enlazarlo con
     * el formulario de login en la vista.
     * </p>
     *
     * @param model modelo para enviar el objeto usuario a la vista
     * @return vista de login
     */
    @RequestMapping("/")
    public String cargarInicio(Model model) {

        model.addAttribute("obj_usu",
                new Usuario(0, "admin", "1234", "ADMIN"));
        return "login";
    }

    /**
     * Cierra la sesión del usuario actual.
     * <p>
     * Invalida la sesión HTTP eliminando todos los atributos almacenados
     * y redirige al inicio de la aplicación.
     * </p>
     *
     * @param session sesión HTTP del usuario
     * @return redirección a la página de inicio
     */
    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
