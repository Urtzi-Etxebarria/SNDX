package com.ipartek.servicios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.ipartek.pojos.Disco;
import com.ipartek.pojos.Genero;

/**
 * Implementación del servicio {@link GeneroServicio} para la gestión de géneros musicales.
 * <p>
 * Esta clase se comunica con una API REST externa para realizar operaciones de
 * CRUD sobre géneros y para obtener los discos asociados a un género específico.
 * Todas las peticiones requieren un token JWT válido.
 * </p>
 */
@Service
public class GeneroServicioImp implements GeneroServicio {

    /** Cliente REST para consumir la API externa */
    private final RestTemplate restTemplate = new RestTemplate();

    /** URL base de la API de géneros */
    private final String URL = "http://localhost:9090/api/generos";

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Genero> obtenerTodosGeneros(String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Genero[]> response = restTemplate.exchange(
            URL,
            HttpMethod.GET,
            entity,
            Genero[].class
        );

        Genero[] generos = response.getBody();

        return generos != null ? Arrays.asList(generos) : new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Genero obtenerGeneroPorId(int id, String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String urlId = URL + "/" + id;

        ResponseEntity<Genero> response = restTemplate.exchange(
            urlId,
            HttpMethod.GET,
            entity,
            Genero.class
        );

        return response.getBody();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modificarGenero(Genero genero, String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<Genero> entity = new HttpEntity<>(genero, headers);

        restTemplate.exchange(
            URL,
            HttpMethod.PUT,
            entity,
            Void.class
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertarGenero(Genero genero, String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<Genero> entity = new HttpEntity<>(genero, headers);

        restTemplate.exchange(
            URL,
            HttpMethod.POST,
            entity,
            Genero.class
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void borrarGenero(Integer id, String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String urlId = URL + "/" + id;

        restTemplate.exchange(
            urlId,
            HttpMethod.DELETE,
            entity,
            Void.class
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Disco> obtenerDiscosPorGenero(int idGenero, String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = URL + "/" + idGenero + "/discos";

        ResponseEntity<Disco[]> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            Disco[].class
        );

        Disco[] discos = response.getBody();

        return discos != null ? Arrays.asList(discos) : new ArrayList<>();
    }

}
