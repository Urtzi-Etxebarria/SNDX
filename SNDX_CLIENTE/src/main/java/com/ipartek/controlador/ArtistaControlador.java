package com.ipartek.controlador;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.ipartek.pojos.Artista;
import com.ipartek.pojos.Disco;
import com.ipartek.servicios.ArtistaServicio;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Controlador MVC encargado de gestionar las operaciones relacionadas
 * con los artistas de la aplicación.
 * <p>
 * Permite:
 * <ul>
 *   <li>Listar todos los artistas</li>
 *   <li>Mostrar el detalle de un artista y su discografía</li>
 *   <li>Gestionar artistas desde el panel de administración</li>
 *   <li>Insertar nuevos artistas junto con su imagen</li>
 * </ul>
 * </p>
 * <p>
 * Las peticiones requieren que el usuario esté autenticado mediante
 * un token JWT almacenado en la sesión HTTP.
 * </p>
 */
@Controller
public class ArtistaControlador {

    /**
     * Ruta del sistema de archivos donde se almacenan las imágenes de los artistas.
     * <p>
     * Se inyecta desde el archivo {@code application.properties}.
     * </p>
     */
    @Value("${ruta.imagenes.artistas}")
    String rutaFotos;

    /**
     * Servicio encargado de la lógica de negocio relacionada con los artistas.
     */
    @Autowired
    private ArtistaServicio artistaServicio;

    /**
     * Logger para el registro de mensajes y errores del controlador.
     */
    private static final Logger logger = LoggerFactory.getLogger(ArtistaControlador.class);

    /**
     * Muestra la lista de todos los artistas disponibles.
     * <p>
     * Para cada artista se carga también su discografía.
     * </p>
     *
     * @param session sesión HTTP del usuario
     * @param model modelo utilizado para enviar datos a la vista
     * @return vista con el listado de artistas o redirección al inicio si no hay sesión
     */
    @GetMapping("/artistas")
    public String listaTodosArtistas(HttpSession session, Model model) {
        String token = (String) session.getAttribute("jwt_token");

        if (token == null) {
            return "redirect:/";
        }

        List<Artista> listaArtistas = artistaServicio.obtenerTodosArtistas(token);

        for (Artista artista : listaArtistas) {
            List<Disco> discografia = artistaServicio.obtenerDiscografiaPorArtista(artista.getId(), token);
            artista.setDiscografia(discografia);
        }

        model.addAttribute("listaArtistas", listaArtistas);
        return "artistas";
    }

    /**
     * Muestra la ficha detallada de un artista concreto.
     *
     * @param id identificador del artista
     * @param session sesión HTTP del usuario
     * @param model modelo para enviar el artista a la vista
     * @return vista con la información del artista o redirección al inicio
     */
    @GetMapping("/artistas/{id}")
    public String verArtistaPorId(@PathVariable("id") int id,
                                 HttpSession session,
                                 Model model) {

        String token = (String) session.getAttribute("jwt_token");

        if (token == null) {
            return "redirect:/";
        }

        Artista artista = artistaServicio.obtenerArtistaPorId(id, token);
        model.addAttribute("artista", artista);

        return "ficha_artista";
    }

    /**
     * Muestra la vista de administración de artistas.
     * <p>
     * Incluye el listado de artistas y un objeto vacío para el formulario
     * de creación.
     * </p>
     *
     * @param session sesión HTTP del usuario
     * @param model modelo para enviar datos a la vista
     * @return vista de administración de artistas
     */
    @GetMapping("/administracion/artistas")
    public String mostrarArtistasAdmin(HttpSession session, Model model) {
        String token = (String) session.getAttribute("jwt_token");

        if (token == null) {
            return "redirect:/";
        }

        model.addAttribute("listaArtistas", artistaServicio.obtenerTodosArtistas(token));
        model.addAttribute("obj_artista", new Artista());

        return "artistas_crud";
    }

    /**
     * Guarda un nuevo artista en el sistema.
     * <p>
     * Permite enviar los datos del artista junto con un archivo de imagen.
     * La inserción se realiza a través del servicio REST.
     * </p>
     *
     * @param session sesión HTTP del usuario
     * @param model modelo para mostrar mensajes de error
     * @param obj_artista objeto artista recibido desde el formulario
     * @param archivo archivo de imagen del artista (opcional)
     * @return redirección al listado de artistas o vista de error
     */
    @PostMapping("/GuardarArtista")
    public String guardarArtista(HttpSession session,
                                 Model model,
                                 @ModelAttribute Artista obj_artista,
                                 @RequestParam(name = "foto2", required = false)
                                 MultipartFile archivo) {

        String token = (String) session.getAttribute("jwt_token");
        if (token == null) {
            return "redirect:/";
        }

        try {
            artistaServicio.insertarArtista(obj_artista, archivo, token);
        } catch (IOException e) {
            logger.error("Error al insertar el artista", e);
            model.addAttribute("error", "No se pudo insertar el artista.");
            return "error";
        }

        return "redirect:/artistas";
    }
}
