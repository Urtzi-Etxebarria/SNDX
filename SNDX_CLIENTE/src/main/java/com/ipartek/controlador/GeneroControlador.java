package com.ipartek.controlador;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.ipartek.pojos.Disco;
import com.ipartek.pojos.Genero;
import com.ipartek.servicios.GeneroServicio;
import jakarta.servlet.http.HttpSession;

/**
 * Controlador MVC encargado de gestionar las operaciones relacionadas
 * con los géneros musicales de la aplicación.
 * <p>
 * Permite:
 * <ul>
 *   <li>Listar todos los géneros</li>
 *   <li>Mostrar el detalle de un género concreto</li>
 *   <li>Gestionar géneros desde el panel de administración</li>
 * </ul>
 * </p>
 * <p>
 * Todas las operaciones requieren autenticación mediante un token JWT
 * almacenado en la sesión HTTP.
 * </p>
 */
@Controller
public class GeneroControlador {

    /**
     * Servicio encargado de la lógica de negocio de los géneros musicales.
     */
    @Autowired
    private GeneroServicio generoServicio;

    /**
     * Muestra el listado de todos los géneros musicales.
     * <p>
     * De forma opcional, se cargan también los discos asociados a cada género.
     * </p>
     *
     * @param session sesión HTTP del usuario
     * @param model modelo para enviar la lista de géneros a la vista
     * @return vista con el listado de géneros o redirección al inicio
     */
    @GetMapping("/generos")
    public String listarGeneros(HttpSession session, Model model) {
        String token = (String) session.getAttribute("jwt_token");

        if (token == null) {
            return "redirect:/";
        }

        List<Genero> listaGeneros = generoServicio.obtenerTodosGeneros(token);

        for (Genero genero : listaGeneros) {
            List<Disco> discos = generoServicio.obtenerDiscosPorGenero(genero.getId(), token);
            genero.setDiscos(discos);
        }

        model.addAttribute("listaGeneros", listaGeneros);
        return "generos";
    }

    /**
     * Muestra el detalle de un género concreto.
     *
     * @param id identificador del género
     * @param session sesión HTTP del usuario
     * @param model modelo para enviar el género a la vista
     * @return vista con la información del género o redirección al inicio
     */
    @GetMapping("/generos/{id}")
    public String verGenero(@PathVariable("id") int id,
                            HttpSession session,
                            Model model) {

        String token = (String) session.getAttribute("jwt_token");

        if (token == null) {
            return "redirect:/";
        }

        Genero genero = generoServicio.obtenerGeneroPorId(id, token);
        model.addAttribute("genero", genero);

        return "generos";
    }

    /**
     * Muestra la vista de administración de géneros.
     *
     * @param session sesión HTTP del usuario
     * @param model modelo para enviar datos a la vista
     * @return vista de administración de géneros
     */
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
