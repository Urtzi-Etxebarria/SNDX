package com.ipartek.componente;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	 private final String SECRET_KEY = "Lamayorpartedeloshombresnoquierennadarantesdesaber¿noesestoespiritual?ynoquierennadar,naturalmente!Hannacidoparalatierra,noparaelaguay,naturalmente,noquierenpensar,comoquehansidocreadosparalavidanoparapensar.Claroyelquepiensa,elquehacedelpensarloprincipalesepodráacasollegarmuylejosenesto,peroeseprecisamentehaconfundidoelaguaconlatierra,ytardeotempranoseahogara.";


	 public Claims extractClaims(String token) {
	        return Jwts.parserBuilder()
	                   .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
	                   .build()
	                   .parseClaimsJws(token)
	                   .getBody();
	    }

	    public boolean isTokenValid(String token) {
	        try {
	        	System.out.println("################################################3");
	            Claims claims = extractClaims(token);
	            return claims.getExpiration().after(new Date());
	        } catch (Exception e) {
	            return false;
	        }
	    }
}
