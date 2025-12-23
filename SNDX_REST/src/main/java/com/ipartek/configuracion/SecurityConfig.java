package com.ipartek.configuracion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ipartek.componente.JwtRequestFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private JwtRequestFilter jwtFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    return http
	            .csrf(csrf -> csrf.disable())
	            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	            .authorizeHttpRequests(auth -> auth
	            		
	                    // ======= DISCO =======
	                    .requestMatchers(HttpMethod.GET, "/api/discos/**").hasAnyRole("ADMIN", "BOSS", "USER")
	                    .requestMatchers(HttpMethod.POST, "/api/discos").hasAnyRole("ADMIN", "BOSS", "USER")
	                    .requestMatchers(HttpMethod.PUT, "/api/discos").hasAnyRole("ADMIN", "BOSS")
	                    .requestMatchers(HttpMethod.DELETE, "/api/discos/**").hasRole("ADMIN")

	                    // ======= DISCOGRAFICA =======
	                    .requestMatchers(HttpMethod.GET, "/api/discograficas/**").hasAnyRole("ADMIN", "BOSS", "USER")
	                    .requestMatchers(HttpMethod.POST, "/api/discograficas").hasAnyRole("ADMIN", "BOSS", "USER")
	                    .requestMatchers(HttpMethod.PUT, "/api/discograficas").hasAnyRole("ADMIN", "BOSS")
	                    .requestMatchers(HttpMethod.DELETE, "/api/discograficas/**").hasRole("ADMIN")

	                    // ======= PRODUCTOR =======
	                    .requestMatchers(HttpMethod.GET, "/api/productores/**").hasAnyRole("ADMIN", "BOSS", "USER")
	                    .requestMatchers(HttpMethod.POST, "/api/productores").hasAnyRole("ADMIN", "BOSS", "USER")
	                    .requestMatchers(HttpMethod.PUT, "/api/productores").hasAnyRole("ADMIN", "BOSS")
	                    .requestMatchers(HttpMethod.DELETE, "/api/productores/**").hasRole("ADMIN")

	                    // ======= ARTISTA =======
	                    .requestMatchers(HttpMethod.GET, "/api/artistas/**").hasAnyRole("ADMIN", "BOSS", "USER")
	                    .requestMatchers(HttpMethod.POST, "/api/artistas").hasAnyRole("ADMIN", "BOSS", "USER")
	                    .requestMatchers(HttpMethod.PUT, "/api/artistas").hasAnyRole("ADMIN", "BOSS")
	                    .requestMatchers(HttpMethod.DELETE, "/api/artistas/**").hasRole("ADMIN")

	                    // ======= SWAGGER =======
	                    .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

	                    // ======= RESTO =======
	                    .anyRequest().authenticated()
	            )
	            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
	            .build();
	}

}
