package com.ipartek.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;

/**
 * Controlador encargado de gestionar las vistas relacionadas con la
 * administración de la aplicación.
 * <p>
 * Atiende las peticiones HTTP dirigidas a la ruta {@code /administracion}
 * y devuelve la vista correspondiente al panel de administración.
 * </p>
 */
@Controller
public class AdministracionControlador {

    /**
     * Muestra la página principal de administración.
     * <p>
     * Este método gestiona las peticiones GET a la URL {@code /administracion}.
     * Puede utilizar la sesión HTTP para comprobar información del usuario
     * autenticado y el {@link Model} para enviar datos a la vista.
     * </p>
     *
     * @param session sesión HTTP del usuario
     * @param model modelo utilizado para pasar atributos a la vista
     * @return nombre de la vista de administración
     */
    @GetMapping("/administracion")
    public String inicioAdmin(HttpSession session, Model model) {
        return "administracion";
    }
}
