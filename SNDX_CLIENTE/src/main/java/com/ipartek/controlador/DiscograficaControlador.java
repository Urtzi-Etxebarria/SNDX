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

/**
 * Controlador MVC encargado de gestionar las operaciones relacionadas
 * con las discográficas de la aplicación.
 * <p>
 * Permite:
 * <ul>
 *   <li>Listar discográficas</li>
 *   <li>Mostrar la ficha detallada de una discográfica</li>
 *   <li>Gestionar discográficas desde el panel de administración</li>
 *   <li>Insertar nuevas discográficas junto con su imagen</li>
 * </ul>
 * </p>
 * <p>
 * Todas las operaciones requieren autenticación mediante un token JWT
 * almacenado en la sesión HTTP.
 * </p>
 */
@Controller
public class DiscograficaControlador {

    /**
     * Servicio encargado de la lógica de negocio de las discográficas.
     */
    @Autowired
    private DiscograficaServicio discograficaServicio;

    /**
     * Logger para el registro de información y errores del controlador.
     */
    private static final Logger logger =
            LoggerFactory.getLogger(DiscograficaControlador.class);

    /**
     * Muestra el listado de todas las discográficas.
     * <p>
     * Para cada discográfica se carga también su discografía asociada.
     * </p>
     *
     * @param session sesión HTTP del usuario
     * @param model modelo para enviar la lista de discográficas a la vista
     * @return vista con el listado de discográficas o redirección al inicio
     */
    @GetMapping("/discograficas")
    public String listarDiscograficas(HttpSession session, Model model) {
        String token = (String) session.getAttribute("jwt_token");

        if (token == null) {
            return "redirect:/";
        }

        List<Discografica> listaDiscograficas =
                discograficaServicio.obtenerTodasDiscograficas(token);

        for (Discografica discografica : listaDiscograficas) {
            List<Disco> discografia = discograficaServicio.obtenerDiscografiaPorDiscografica(discografica.getId(), token);
            discografica.setDiscografia(discografia);
        }

        model.addAttribute("listaDiscograficas", listaDiscograficas);
        return "discograficas";
    }

    /**
     * Muestra la ficha detallada de una discográfica concreta.
     *
     * @param id identificador de la discográfica
     * @param session sesión HTTP del usuario
     * @param model modelo para enviar la discográfica y su discografía a la vista
     * @return vista con la información de la discográfica o redirección al inicio
     */
    @GetMapping("/discograficas/{id}")
    public String verDiscografica(@PathVariable("id") int id,
                                  HttpSession session,
                                  Model model) {

        String token = (String) session.getAttribute("jwt_token");

        if (token == null) {
            return "redirect:/";
        }

        Discografica discografica =
                discograficaServicio.obtenerDiscograficaPorId(id, token);

        model.addAttribute("discografica", discografica);
        model.addAttribute("listaDiscografia", discograficaServicio.obtenerDiscografiaPorDiscografica(id, token));

        return "ficha_discografica";
    }

    /**
     * Muestra la vista de administración de discográficas.
     *
     * @param session sesión HTTP del usuario
     * @param model modelo para enviar datos a la vista
     * @return vista de administración de discográficas
     */
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

    /**
     * Guarda una nueva discográfica en el sistema.
     * <p>
     * Recibe los datos desde el formulario y permite adjuntar una imagen.
     * La inserción se realiza a través del servicio REST.
     * </p>
     *
     * @param session sesión HTTP del usuario
     * @param model modelo para mostrar mensajes de error
     * @param obj_discografica objeto discográfica recibido desde el formulario
     * @param archivo archivo de imagen de la discográfica (opcional)
     * @return redirección al listado de discográficas o vista de error
     */
    @PostMapping("/GuardarDiscografica")
    public String guardarDiscografica(HttpSession session,
                                      Model model,
                                      @ModelAttribute Discografica obj_discografica,
                                      @RequestParam(name = "foto2", required = false)
                                      MultipartFile archivo) {

        String token = (String) session.getAttribute("jwt_token");
        if (token == null) {
            return "redirect:/";
        }

        try {
            discograficaServicio.insertarDiscografica(obj_discografica, archivo, token);
        } catch (IOException e) {
            logger.error("Error al insertar la discográfica", e);
            model.addAttribute("error", "No se pudo insertar la discográfica.");
            return "error";
        }

        return "redirect:/discograficas";
    }
}
