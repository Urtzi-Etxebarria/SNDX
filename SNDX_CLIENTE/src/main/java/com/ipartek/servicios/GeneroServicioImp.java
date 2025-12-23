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

@Service
public class GeneroServicioImp implements GeneroServicio {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String URL = "http://localhost:9090/api/generos";

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

