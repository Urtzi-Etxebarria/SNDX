package com.ipartek.componentes;

import java.util.Date;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * Utilidad para la generación y validación de tokens JWT (JSON Web Token).
 * <p>
 * Esta clase se encarga de:
 * <ul>
 *   <li>Generar tokens JWT firmados con algoritmo HS256</li>
 *   <li>Incluir información del usuario (username y rol) dentro del token</li>
 *   <li>Extraer y validar los claims contenidos en un JWT</li>
 * </ul>
 * </p>
 * <p>
 * Está marcada como {@link Component} para que Spring la detecte automáticamente
 * y permita su inyección mediante {@code @Autowired}.
 * </p>
 */
@Component
public class JwtUtil {

    /**
     * Clave secreta utilizada para firmar y validar los tokens JWT.
     * <p>
     * Debe tener una longitud suficiente para el algoritmo HS256.
     * En un entorno real, esta clave debería almacenarse en una
     * variable de entorno o en un sistema de configuración seguro,
     * y no directamente en el código fuente.
     * </p>
     */
    private final String SECRET_KEY =
            "Lamayorpartedeloshombresnoquierennadarantesdesaber¿noesestoespiritual?"
          + "ynoquierennadar,naturalmente!Hannacidoparalatierra,noparaelagua"
          + "ynaturalmente,noquierenpensar,comoquehansidocreadosparalavida"
          + "noparapensar.Claroyelquepiensa,elquehacedelpensarloprincipal"
          + "esepodráacasollegarmuylejosenesto,peroeseprecisamentehaconfundido"
          + "elaguaconlatierra,ytardeotempranoseahogara.";

    /**
     * Genera un token JWT para un usuario concreto.
     * <p>
     * El token incluye:
     * <ul>
     *   <li>El nombre de usuario como {@code subject}</li>
     *   <li>El rol del usuario como claim personalizado</li>
     *   <li>La fecha de emisión</li>
     *   <li>La fecha de expiración (24 horas)</li>
     * </ul>
     * </p>
     *
     * @param username nombre de usuario que se incluirá como subject del token
     * @param role rol del usuario que se añadirá como claim
     * @return token JWT generado y firmado
     */
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("rol", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000 * 24))
                .signWith(
                        Keys.hmacShaKeyFor(SECRET_KEY.getBytes()),
                        SignatureAlgorithm.HS256
                )
                .compact();
    }

    /**
     * Extrae y devuelve los claims contenidos en un token JWT.
     * <p>
     * Este método valida la firma del token utilizando la misma
     * clave secreta con la que fue generado. Si el token es inválido
     * o ha expirado, se lanzará una excepción.
     * </p>
     *
     * @param token token JWT del cual se desean extraer los datos
     * @return objeto {@link Claims} con la información contenida en el token
     */
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
