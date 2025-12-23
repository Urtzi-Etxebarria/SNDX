package com.ipartek.componente;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {	
    	//Ignorar Swagger/OpenAPI
    	String path = request.getRequestURI();

   	 	if (path.startsWith("/v3/api-docs")
   	 			|| path.startsWith("/swagger-ui")
   	 			|| path.equals("/swagger-ui.html")
   	 			|| path.startsWith("/swagger-ui/index.html")) {
   	 		try {
   	 			chain.doFilter(request, response);
			} catch (java.io.IOException | ServletException e) {
				e.printStackTrace();
			}
           return;
       }
       //Fin de ignorar swagger

        final String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization header: " + authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            System.out.println("Token recibido: " + token);

            if (jwtUtil.isTokenValid(token)) {
                Claims claims = jwtUtil.extractClaims(token);
                String username = claims.getSubject();
                String rol = claims.get("rol", String.class);
                System.out.println("Token válido para usuario: " + username + ", rol: " + rol);

                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + rol));
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
            	System.out.println("Token inválido o expirado.");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        } else {
        	System.out.println("No se encontró cabecera Authorization o no comienza con Bearer");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
			chain.doFilter(request, response);
		} catch (java.io.IOException | ServletException e) {
			e.printStackTrace();
		}
    }
}
