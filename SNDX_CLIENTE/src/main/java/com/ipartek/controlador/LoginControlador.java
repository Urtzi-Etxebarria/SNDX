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
 
@Controller
public class LoginControlador {
	
	@Autowired
	private ArtistaServicio artistaServicio;
	
	@Autowired
	private DiscoServicio discoServicio;
	
	@Autowired
	private DiscograficaServicio discograficaServicio;
	
	@Autowired
	private ProductorServicio productorServicio;
	
	@Autowired
	private GeneroServicio generoServicio;
	
	@Autowired
	private UsuarioRepositorio usuarioRepo;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@PostMapping("/login")
	public String validarUsuario(
	        @ModelAttribute("obj_usu") Usuario obj_usu,
	        Model model,
	        HttpSession session) {

	    Optional<Usuario> usuario = usuarioRepo.findByName(obj_usu.getName());

	    if (usuario.isPresent() && usuario.get().getPass().equals(obj_usu.getPass())) {
	        String token = jwtUtil.generateToken(usuario.get().getName(), usuario.get().getRole());

	        session.setAttribute("jwt_token", token);
	        session.setAttribute("rol", usuario.get().getRole());
	        session.setAttribute("name", usuario.get().getName());

	        String jwtToken = token; // Guardamos el token en variable local

	        // Obtener entidades completas (no solo strings)
	        Disco discoSemana = discoServicio.obtenerDiscoAleatorio(jwtToken);
	        Artista artistaSemana = artistaServicio.obtenerArtistaAleatorio(jwtToken);
	        Productor productorSemana = productorServicio.obtenerProductorAleatorio(jwtToken);
	        Discografica discograficaSemana = discograficaServicio.obtenerDiscograficaAleatorio(jwtToken);

	        model.addAttribute("obj_disco", new Disco());
	        model.addAttribute("listaDiscos", discoServicio.obtenerTodosDiscos(jwtToken));
	        model.addAttribute("listaArtistas", artistaServicio.obtenerTodosArtistas(jwtToken));
	        model.addAttribute("listaDiscograficas", discograficaServicio.obtenerTodasDiscograficas(jwtToken));
	        model.addAttribute("listaProductores", productorServicio.obtenerTodosProductores(jwtToken));
	        model.addAttribute("listaGeneros", generoServicio.obtenerTodosGeneros(jwtToken));
	        
	        // Agregar las entidades completas
	        model.addAttribute("discoSemana", discoSemana);
	        model.addAttribute("artistaSemana", artistaSemana);
	        model.addAttribute("productorSemana", productorSemana);
	        model.addAttribute("discograficaSemana", discograficaSemana);
	        
	        // Ahora puedes obtener las discografías usando los IDs
	        if (discograficaSemana != null) {
	            model.addAttribute("discograficaSemanaDiscografia", 
	                discograficaServicio.obtenerDiscografiaPorDiscografica(
	                    discograficaSemana.getId(), jwtToken));
	        }
	        
	        if (artistaSemana != null) {
	            model.addAttribute("artistaSemanaDiscografia", 
	                artistaServicio.obtenerDiscografiaPorArtista(
	                    artistaSemana.getId(), jwtToken));
	        }
	        
	        if (productorSemana != null) {
	            model.addAttribute("productorSemanaDiscografia", 
	                productorServicio.obtenerDiscografiaPorProductor(
	                    productorSemana.getId(), jwtToken));
	        }

	        return "home";
	    }

	    model.addAttribute("error", "Credenciales incorrectas");
	    return "redirect:/";
	}
	
}