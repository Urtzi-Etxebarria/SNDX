package com.ipartek.componentes;

import java.util.Date;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component  // [1] Marca esta clase como un componente Spring (detectable automáticamente para inyección de dependencias)
public class JwtUtil {

    // [2] Clave secreta para firmar el JWT (debe tener una longitud adecuada para HS256, por eso es tan larga)
    private final String SECRET_KEY = "Lamayorpartedeloshombresnoquierennadarantesdesaber¿noesestoespiritual?ynoquierennadar,naturalmente!Hannacidoparalatierra,noparaelaguay,naturalmente,noquierenpensar,comoquehansidocreadosparalavidanoparapensar.Claroyelquepiensa,elquehacedelpensarloprincipalesepodráacasollegarmuylejosenesto,peroeseprecisamentehaconfundidoelaguaconlatierra,ytardeotempranoseahogara.";

    // [3] Método para generar un token JWT con el nombre de usuario y su rol
    public String generateToken(String username, String role) {
        return Jwts.builder()                                      					// [4] Comienza a construir el JWT
                .setSubject(username)                              					// [5] Define el "subject" del token (normalmente el usuario)
                .claim("rol", role)                                					// [6] Añade un "claim" personalizado (rol del usuario)
                .setIssuedAt(new Date())                           					// [7] Fecha y hora en que se creó el token
                .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000*24))  // [8] Fecha de expiración: ahora + 1 hora (en milisegundos)
                .signWith(                                         					// [9] Firma el token con algoritmo HS256 y la clave secreta
                    Keys.hmacShaKeyFor(SECRET_KEY.getBytes()),    					// [10] Convierte la clave secreta en bytes para firmar
                    SignatureAlgorithm.HS256                       					// [11] Algoritmo de firma: HMAC con SHA-256
                )
                .compact();                                        					// [12] Genera el token como una cadena compacta
    }

    																		
    public Claims extractClaims(String token) {								 // [13] Método para extraer todos los "claims" (datos) del token
        return Jwts.parserBuilder()                                			 // [14] Comienza el proceso de parseo del token
                   .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes())) // [15] Usa la misma clave secreta para verificar la firma
                   .build()                                        			 // [16] Construye el validador del JWT
                   .parseClaimsJws(token)                                    // [17] Analiza el JWT y lo valida
                   .getBody();                                               // [18] Extrae y devuelve los claims (datos del token)
    }
}