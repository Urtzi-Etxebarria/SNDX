package com.ipartek.servicios;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipartek.pojos.Disco;
import com.ipartek.pojos.ErrorMsg;

/**
 * Implementación de {@link DiscoServicio} que utiliza REST para comunicarse
 * con un backend remoto.
 * <p>
 * Este servicio permite obtener, insertar, modificar y eliminar discos,
 * así como seleccionar un disco aleatorio. Todas las operaciones requieren
 * un token JWT válido.
 * </p>
 */
@Service
public class DiscoServicioImp implements DiscoServicio {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String URL = "http://localhost:9090/api/discos";

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Disco> obtenerTodosDiscos(String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Disco[]> response = restTemplate.exchange(
            URL,
            HttpMethod.GET,
            entity,
            Disco[].class
        );

        Disco[] disco = response.getBody();
        return Arrays.asList(disco);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Disco obtenerDiscoPorId(int id, String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String URLID = URL + "/" + id;

        ResponseEntity<Disco> response = restTemplate.exchange(
            URLID,
            HttpMethod.GET,
            entity,
            Disco.class
        );

        return response.getBody();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modificarDisco(Disco disco, String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<Disco> entity = new HttpEntity<>(disco, headers);

        restTemplate.exchange(URL, HttpMethod.PUT, entity, Void.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertarDisco(Disco disco, MultipartFile archivo, String jwtToken) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        ByteArrayResource discoJson = new ByteArrayResource(
            new ObjectMapper().writeValueAsBytes(disco)) {
            @Override
            public String getFilename() {
                return "disco.json";
            }
        };
        body.add("disco", discoJson);

        if (archivo != null && !archivo.isEmpty()) {
            body.add("foto2", new ByteArrayResource(archivo.getBytes()) {
                @Override
                public String getFilename() {
                    return archivo.getOriginalFilename();
                }
            });
        }

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<ErrorMsg> response = restTemplate.exchange(
            URL,
            HttpMethod.POST,
            requestEntity,
            ErrorMsg.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Error al insertar disco: " + response.getStatusCode());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void borrarDisco(Integer id, String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String urlId = URL + "/" + id;
        restTemplate.exchange(urlId, HttpMethod.DELETE, entity, Void.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Disco obtenerDiscoAleatorio(String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Disco[]> response = restTemplate.exchange(
            URL,
            HttpMethod.GET,
            entity,
            Disco[].class
        );

        Disco[] disco = response.getBody();

        if (disco == null || disco.length == 0) {
            return null;
        }

        Random random = new Random();
        int indice = random.nextInt(disco.length);
        return disco[indice];
    }
}
