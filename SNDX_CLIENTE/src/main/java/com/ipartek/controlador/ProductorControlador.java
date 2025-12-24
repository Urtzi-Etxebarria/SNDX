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

/**
 * Controlador MVC encargado de gestionar las operaciones relacionadas
 * con los productores musicales de la aplicación.
 * <p>
 * Permite:
 * <ul>
 *   <li>Listar todos los productores</li>
 *   <li>Mostrar la ficha detallada de un productor</li>
 *   <li>Gestionar productores desde el panel de administración</li>
 *   <li>Insertar nuevos productores junto con su imagen</li>
 * </ul>
 * </p>
 * <p>
 * Todas las operaciones requieren autenticación mediante un token JWT
 * almacenado en la sesión HTTP.
 * </p>
 */
@Controller
public class ProductorControlador {

    /**
     * Servicio encargado de la lógica de negocio de los productores.
     */
    @Autowired
    private ProductorServicio productorServicio;

    /**
     * Logger para el registro de información y errores del controlador.
     */
    private static final Logger logger =
            LoggerFactory.getLogger(ProductorControlador.class);

    /**
     * Muestra el listado de todos los productores.
     * <p>
     * Para cada productor se carga también su discografía asociada.
     * </p>
     *
     * @param session sesión HTTP del usuario
     * @param model modelo para enviar la lista de productores a la vista
     * @return vista con el listado de productores o redirección al inicio
     */
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

    /**
     * Muestra la ficha detallada de un productor concreto.
     *
     * @param id identificador del productor
     * @param session sesión HTTP del usuario
     * @param model modelo para enviar el productor y su discografía a la vista
     * @return vista con la información del productor o redirección al inicio
     */
    @GetMapping("/productores/{id}")
    public String verProductor(@PathVariable("id") int id,
                               HttpSession session,
                               Model model) {

        String token = (String) session.getAttribute("jwt_token");

        if (token == null) {
            return "redirect:/";
        }

        Productor productor = productorServicio.obtenerProductorPorId(id, token);
        model.addAttribute("productor", productor);
        model.addAttribute("listaDiscografia", productorServicio.obtenerDiscografiaPorProductor(id, token));

        return "ficha_productor";
    }

    /**
     * Muestra la vista de administración de productores.
     *
     * @param session sesión HTTP del usuario
     * @param model modelo para enviar datos a la vista
     * @return vista de administración de productores
     */
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

    /**
     * Guarda un nuevo productor en el sistema.
     * <p>
     * Recibe los datos desde el formulario y permite adjuntar una imagen.
     * La inserción se realiza a través del servicio REST.
     * </p>
     *
     * @param session sesión HTTP del usuario
     * @param model modelo para mostrar mensajes de error
     * @param obj_productor objeto productor recibido desde el formulario
     * @param archivo archivo de imagen del productor (opcional)
     * @return redirección al listado de productores o vista de error
     */
    @PostMapping("/GuardarProductor")
    public String guardarProductor(HttpSession session,
                                   Model model,
                                   @ModelAttribute Productor obj_productor,
                                   @RequestParam(name = "foto2", required = false)
                                   MultipartFile archivo) {

        String token = (String) session.getAttribute("jwt_token");
        if (token == null) {
            return "redirect:/";
        }

        try {
            productorServicio.insertarProductor(obj_productor, archivo, token);
        } catch (IOException e) {
            logger.error("Error al insertar el productor", e);
            model.addAttribute("error", "No se pudo insertar el productor.");
            return "error";
        }

        return "redirect:/productores";
    }
}
