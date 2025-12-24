package com.ipartek.controlador;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.ipartek.componentes.JwtUtil;
import com.ipartek.modelo.Usuario;
import com.ipartek.pojos.Artista;
import com.ipartek.pojos.Disco;
import com.ipartek.pojos.Discografica;
import com.ipartek.pojos.Productor;
import com.ipartek.repositorio.UsuarioRepositorio;
import com.ipartek.servicios.ArtistaServicio;
import com.ipartek.servicios.DiscoServicio;
import com.ipartek.servicios.DiscograficaServicio;
import com.ipartek.servicios.GeneroServicio;
import com.ipartek.servicios.ProductorServicio;
import jakarta.servlet.http.HttpSession;

/**
 * Controlador MVC encargado de gestionar el proceso de autenticación
 * de los usuarios en la aplicación.
 * <p>
 * Se encarga de:
 * <ul>
 *   <li>Validar las credenciales del usuario</li>
 *   <li>Generar un token JWT en caso de autenticación correcta</li>
 *   <li>Guardar la información de sesión del usuario</li>
 *   <li>Cargar los datos iniciales de la página principal</li>
 * </ul>
 * </p>
 */
@Controller
public class LoginControlador {

    /**
     * Servicio encargado de la gestión de artistas.
     */
    @Autowired
    private ArtistaServicio artistaServicio;

    /**
     * Servicio encargado de la gestión de discos.
     */
    @Autowired
    private DiscoServicio discoServicio;

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
     * Repositorio encargado del acceso a los usuarios registrados.
     */
    @Autowired
    private UsuarioRepositorio usuarioRepo;

    /**
     * Utilidad para la generación de tokens JWT.
     */
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Valida las credenciales de un usuario y gestiona el inicio de sesión.
     * <p>
     * Si las credenciales son correctas:
     * <ul>
     *   <li>Se genera un token JWT</li>
     *   <li>Se almacenan los datos del usuario en la sesión</li>
     *   <li>Se cargan los datos necesarios para la vista principal</li>
     * </ul>
     * </p>
     * <p>
     * En caso contrario, se redirige a la página de login con un mensaje de error.
     * </p>
     *
     * @param obj_usu objeto {@link Usuario} recibido desde el formulario de login
     * @param model modelo para enviar datos a la vista
     * @param session sesión HTTP del usuario
     * @return vista principal si el login es correcto o redirección al login
     */
    @PostMapping("/login")
    public String validarUsuario(@ModelAttribute("obj_usu") Usuario obj_usu,
                                 Model model,
                                 HttpSession session) {

        Optional<Usuario> usuario = usuarioRepo.findByName(obj_usu.getName());

        if (usuario.isPresent() && usuario.get().getPass().equals(obj_usu.getPass())) {

            String token = jwtUtil.generateToken(usuario.get().getName(), usuario.get().getRole());

            session.setAttribute("jwt_token", token);
            session.setAttribute("rol", usuario.get().getRole());
            session.setAttribute("name", usuario.get().getName());

            // Obtener elementos destacados para la página principal
            Disco discoSemana = discoServicio.obtenerDiscoAleatorio(token);
            Artista artistaSemana = artistaServicio.obtenerArtistaAleatorio(token);
            Productor productorSemana = productorServicio.obtenerProductorAleatorio(token);
            Discografica discograficaSemana = discograficaServicio.obtenerDiscograficaAleatorio(token);

            model.addAttribute("obj_disco", new Disco());
            model.addAttribute("listaDiscos", discoServicio.obtenerTodosDiscos(token));
            model.addAttribute("listaArtistas", artistaServicio.obtenerTodosArtistas(token));
            model.addAttribute("listaDiscograficas", discograficaServicio.obtenerTodasDiscograficas(token));
            model.addAttribute("listaProductores", productorServicio.obtenerTodosProductores(token));
            model.addAttribute("listaGeneros", generoServicio.obtenerTodosGeneros(token));
            model.addAttribute("discoSemana", discoSemana);
            model.addAttribute("artistaSemana", artistaSemana);
            model.addAttribute("productorSemana", productorSemana);
            model.addAttribute("discograficaSemana", discograficaSemana);

            if (discograficaSemana != null) {
                model.addAttribute("discograficaSemanaDiscografia", discograficaServicio.obtenerDiscografiaPorDiscografica(discograficaSemana.getId(), token));
            }

            if (artistaSemana != null) {
                model.addAttribute("artistaSemanaDiscografia", artistaServicio.obtenerDiscografiaPorArtista(artistaSemana.getId(), token));
            }

            if (productorSemana != null) {
                model.addAttribute("productorSemanaDiscografia", productorServicio.obtenerDiscografiaPorProductor(productorSemana.getId(), token));
            }

            return "home";
        }

        model.addAttribute("error", "Credenciales incorrectas");
        return "redirect:/";
    }
}
