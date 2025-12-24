package com.ipartek.servicios;

import java.io.IOException;
import java.util.ArrayList;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipartek.pojos.Disco;
import com.ipartek.pojos.ErrorMsg;
import com.ipartek.pojos.Productor;

/**
 * Implementación del servicio {@link ProductorServicio}.
 * <p>
 * Esta clase gestiona operaciones CRUD sobre productores musicales y su discografía,
 * comunicándose con un servicio REST externo. Todas las operaciones requieren un token JWT válido.
 * </p>
 */
@Service
public class ProductorServicioImp implements ProductorServicio {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String URL = "http://localhost:9090/api/productores";

    /**
     * Obtiene todos los productores registrados en el sistema.
     *
     * @param jwtToken Token JWT para autenticación.
     * @return Lista de productores.
     */
    @Override
    public List<Productor> obtenerTodosProductores(String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Productor[]> response = restTemplate.exchange(
            URL, HttpMethod.GET, entity, Productor[].class
        );

        Productor[] productor = response.getBody();
        return Arrays.asList(productor);
    }

    /**
     * Obtiene un productor por su ID.
     *
     * @param id ID del productor.
     * @param jwtToken Token JWT para autenticación.
     * @return Productor correspondiente al ID.
     */
    @Override
    public Productor obtenerProductorPorId(int id, String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String URLID = URL + "/" + id;
        ResponseEntity<Productor> response = restTemplate.exchange(
            URLID, HttpMethod.GET, entity, Productor.class
        );

        return response.getBody();
    }

    /**
     * Modifica un productor existente.
     *
     * @param productor Productor con los datos actualizados.
     * @param jwtToken Token JWT para autenticación.
     */
    @Override
    public void modificarProductor(Productor productor, String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<Productor> entity = new HttpEntity<>(productor, headers);

        restTemplate.exchange(URL, HttpMethod.PUT, entity, Void.class);
    }

    /**
     * Inserta un nuevo productor con opción de agregar su foto.
     *
     * @param obj_productor Productor a insertar.
     * @param archivo Foto opcional del productor.
     * @param jwtToken Token JWT para autenticación.
     * @throws IOException Si ocurre un error al procesar el archivo.
     */
    @Override
    public void insertarProductor(Productor obj_productor, MultipartFile archivo, String jwtToken) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        ByteArrayResource productorResource = new ByteArrayResource(
            new ObjectMapper().writeValueAsBytes(obj_productor)) {
                @Override
                public String getFilename() { return "productor.json"; }
            };
        body.add("productor", productorResource);

        if (archivo != null && !archivo.isEmpty()) {
            body.add("foto2", new ByteArrayResource(archivo.getBytes()) {
                @Override
                public String getFilename() { return archivo.getOriginalFilename(); }
            });
        }

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<ErrorMsg> response = restTemplate.exchange(
            URL, HttpMethod.POST, requestEntity, ErrorMsg.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Error al insertar productor: " + response.getStatusCode());
        }
    }

    /**
     * Elimina un productor por su ID.
     *
     * @param id ID del productor a eliminar.
     * @param jwtToken Token JWT para autenticación.
     */
    @Override
    public void borrarProductor(Integer id, String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String urlId = URL + "/" + id;
        restTemplate.exchange(urlId, HttpMethod.DELETE, entity, Void.class);
    }

    /**
     * Obtiene la discografía asociada a un productor.
     *
     * @param idProductor ID del productor.
     * @param jwtToken Token JWT para autenticación.
     * @return Lista de discos del productor.
     */
    @Override
    public List<Disco> obtenerDiscografiaPorProductor(int idProductor, String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = URL + "/" + idProductor + "/discografia";

        try {
            ResponseEntity<Disco[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, Disco[].class);
            Disco[] discos = response.getBody();
            return discos != null ? Arrays.asList(discos) : new ArrayList<>();
        } catch (HttpClientErrorException.NotFound e) {
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene un productor aleatorio del sistema.
     *
     * @param jwtToken Token JWT para autenticación.
     * @return Productor seleccionado aleatoriamente o {@code null} si no hay productores.
     */
    @Override
    public Productor obtenerProductorAleatorio(String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Productor[]> response = restTemplate.exchange(
            URL, HttpMethod.GET, entity, Productor[].class
        );

        Productor[] productor = response.getBody();
        if (productor == null || productor.length == 0) return null;

        Random random = new Random();
        int indice = random.nextInt(productor.length);
        return productor[indice];
    }
}
