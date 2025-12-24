package com.ipartek.controlador;

import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.ipartek.pojos.Disco;
import com.ipartek.servicios.ArtistaServicio;
import com.ipartek.servicios.DiscoServicio;
import com.ipartek.servicios.DiscograficaServicio;
import com.ipartek.servicios.GeneroServicio;
import com.ipartek.servicios.ProductorServicio;
import jakarta.servlet.http.HttpSession;

/**
 * Controlador MVC encargado de gestionar las operaciones relacionadas
 * con los discos musicales de la aplicación.
 * <p>
 * Permite:
 * <ul>
 *   <li>Listar discos</li>
 *   <li>Mostrar la ficha detallada de un disco</li>
 *   <li>Gestionar discos desde el panel de administración</li>
 *   <li>Insertar nuevos discos con sus relaciones e imagen</li>
 * </ul>
 * </p>
 * <p>
 * Todas las operaciones requieren autenticación mediante un token JWT
 * almacenado en la sesión HTTP.
 * </p>
 */
@Controller
public class DiscoControlador {

    /**
     * Ruta del sistema de archivos donde se almacenan las imágenes de los discos.
     * <p>
     * Se inyecta desde el archivo {@code application.properties}.
     * </p>
     */
    @Value("${ruta.imagenes.discos}")
    String rutaFotos;

    /**
     * Logger para el registro de información y errores del controlador.
     */
    private static final Logger logger = LoggerFactory.getLogger(DiscoControlador.class);

    /**
     * Servicio encargado de la lógica de negocio de los discos.
     */
    @Autowired
    private DiscoServicio discoServicio;

    /**
     * Servicio encargado de la gestión de artistas.
     */
    @Autowired
    private ArtistaServicio artistaServicio;

    /**
     * Servicio encargado de la gestión de discográficas.
     */
    @Autowired
    private DiscograficaServicio discograficaServicio;

    /**
     * Servicio encargado de la gestión de productores.
     */
    @Autowired
    private ProductorServicio productorServicio;

    /**
     * Servicio encargado de la gestión de géneros musicales.
     */
    @Autowired
    private GeneroServicio generoServicio;

    /**
     * Muestra el listado de todos los discos disponibles.
     *
     * @param session sesión HTTP del usuario
     * @param model modelo para enviar la lista de discos a la vista
     * @return vista de listado de discos o redirección al inicio
     */
    @GetMapping("/discos")
    public String listarDiscos(HttpSession session, Model model) {
        String token = (String) session.getAttribute("jwt_token");

        if (token == null) {
            return "redirect:/";
        }

        List<Disco> listaDiscos = discoServicio.obtenerTodosDiscos(token);
        model.addAttribute("listaDiscos", listaDiscos);

        return "discos";
    }

    /**
     * Muestra la ficha detallada de un disco concreto.
     *
     * @param id identificador del disco
     * @param session sesión HTTP del usuario
     * @param model modelo para enviar el disco a la vista
     * @return vista con la información del disco o redirección al inicio
     */
    @GetMapping("/discos/{id}")
    public String verDisco(@PathVariable("id") int id,
                           HttpSession session,
                           Model model) {

        String token = (String) session.getAttribute("jwt_token");

        if (token == null) {
            return "redirect:/";
        }

        Disco disco = discoServicio.obtenerDiscoPorId(id, token);
        model.addAttribute("disco", disco);

        return "ficha_disco";
    }

    /**
     * Muestra la vista de administración de discos.
     * <p>
     * Proporciona los datos necesarios para el formulario de creación:
     * artistas, discográficas, géneros y productores.
     * </p>
     *
     * @param session sesión HTTP del usuario
     * @param model modelo para enviar datos a la vista
     * @return vista de administración de discos
     */
    @GetMapping("/administracion/discos")
    public String mostrarDiscos(HttpSession session, Model model) {
        String token = (String) session.getAttribute("jwt_token");

        if (token == null) {
            return "redirect:/";
        }

        model.addAttribute("listaDiscos", discoServicio.obtenerTodosDiscos(token));
        model.addAttribute("obj_disco", new Disco());
        model.addAttribute("listaArtistas", artistaServicio.obtenerTodosArtistas(token));
        model.addAttribute("listaDiscograficas", discograficaServicio.obtenerTodasDiscograficas(token));
        model.addAttribute("listaGeneros", generoServicio.obtenerTodosGeneros(token));
        model.addAttribute("listaProductores", productorServicio.obtenerTodosProductores(token));

        return "discos_crud";
    }

    /**
     * Guarda un nuevo disco en el sistema.
     * <p>
     * Recibe los datos del disco desde el formulario, construye el objeto
     * {@link Disco} con sus relaciones y envía la información al servicio REST.
     * </p>
     *
     * @param session sesión HTTP del usuario
     * @param model modelo para mostrar mensajes de error
     * @param nombre nombre del disco
     * @param puntuacion puntuación del disco
     * @param fecha fecha de lanzamiento
     * @param enlaceWikipedia enlace a Wikipedia
     * @param enlaceSpotify enlace a Spotify
     * @param enlaceTidal enlace a Tidal
     * @param artistaId identificador del artista
     * @param discograficaId identificador de la discográfica
     * @param generoId identificador del género
     * @param productorId identificador del productor
     * @param archivo archivo de imagen del disco (opcional)
     * @return redirección al listado de discos o vista de error
     */
    @PostMapping("/GuardarDisco")
    public String guardarDisco(HttpSession session,
                               Model model,
                               @RequestParam("nombre") String nombre,
                               @RequestParam("puntuacion") int puntuacion,
                               @RequestParam("fecha") String fecha,
                               @RequestParam("enlaceWikipedia") String enlaceWikipedia,
                               @RequestParam("enlaceSpotify") String enlaceSpotify,
                               @RequestParam("enlaceTidal") String enlaceTidal,
                               @RequestParam("artista") int artistaId,
                               @RequestParam("discografica") int discograficaId,
                               @RequestParam("genero") int generoId,
                               @RequestParam("productor") int productorId,
                               @RequestParam(name = "foto2", required = false)
                               MultipartFile archivo) {

        String token = (String) session.getAttribute("jwt_token");
        if (token == null) {
            return "redirect:/";
        }

        Disco obj_disco = new Disco();
        obj_disco.setNombre(nombre);
        obj_disco.setPuntuacion(puntuacion);
        obj_disco.setFecha(fecha);
        obj_disco.setEnlaceWikipedia(enlaceWikipedia);
        obj_disco.setEnlaceSpotify(enlaceSpotify);
        obj_disco.setEnlaceTidal(enlaceTidal);
        obj_disco.setArtista(artistaServicio.obtenerArtistaPorId(artistaId, token));
        obj_disco.setDiscografica(discograficaServicio.obtenerDiscograficaPorId(discograficaId, token));
        obj_disco.setGenero(generoServicio.obtenerGeneroPorId(generoId, token));
        obj_disco.setProductor(productorServicio.obtenerProductorPorId(productorId, token));

        try {
            discoServicio.insertarDisco(obj_disco, archivo, token);
        } catch (IOException e) {
            logger.error("Error al insertar el disco", e);
            model.addAttribute("error", "No se pudo insertar el disco.");
            return "error";
        }

        return "redirect:/discos";
    }
}
