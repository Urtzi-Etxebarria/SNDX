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
import com.ipartek.pojos.Artista;
import com.ipartek.pojos.Disco;
import com.ipartek.pojos.ErrorMsg;

/**
 * Implementación del servicio {@link ArtistaServicio} que se encarga
 * de gestionar las operaciones sobre artistas musicales mediante
 * comunicación con un API REST externa.
 * <p>
 * Todas las operaciones requieren un token JWT para autenticación.
 * </p>
 */
@Service
public class ArtistaServicioImp implements ArtistaServicio {

    /**
     * Cliente REST para realizar llamadas HTTP al API externo.
     */
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * URL base del endpoint de artistas.
     */
    private final String URL = "http://localhost:9090/api/artistas";

    /**
     * Obtiene la lista de todos los artistas.
     *
     * @param jwtToken token JWT de autenticación
     * @return lista de artistas
     */
    @Override
    public List<Artista> obtenerTodosArtistas(String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Artista[]> response = restTemplate.exchange(
            URL,
            HttpMethod.GET,
            entity,
            Artista[].class
        );

        Artista[] artista = response.getBody();
        return Arrays.asList(artista);
    }

    /**
     * Obtiene un artista por su identificador.
     *
     * @param id identificador del artista
     * @param jwtToken token JWT de autenticación
     * @return artista correspondiente al ID
     */
    @Override
    public Artista obtenerArtistaPorId(int id, String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        String URLID = URL + "/" + id;

        ResponseEntity<Artista> response = restTemplate.exchange(
            URLID,
            HttpMethod.GET,
            entity,
            Artista.class
        );

        return response.getBody();
    }

    /**
     * Modifica un artista existente en el sistema.
     *
     * @param artista artista con los datos actualizados
     * @param jwtToken token JWT de autenticación
     */
    @Override
    public void modificarArtista(Artista artista, String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<Artista> entity = new HttpEntity<>(artista, headers);

        restTemplate.exchange(
            URL,
            HttpMethod.PUT,
            entity,
            Void.class
        );
    }

    /**
     * Inserta un nuevo artista en el sistema.
     *
     * @param obj_artista artista a insertar
     * @param archivo archivo de imagen del artista (opcional)
     * @param jwtToken token JWT de autenticación
     * @throws IOException si ocurre un error al procesar el archivo
     */
    @Override
    public void insertarArtista(Artista obj_artista, MultipartFile archivo, String jwtToken) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        ByteArrayResource artistaResource = new ByteArrayResource(
            new ObjectMapper().writeValueAsBytes(obj_artista)) {
                @Override
                public String getFilename() {
                    return "artista.json";
                }
            };
        body.add("artista", artistaResource);

        if (archivo != null && !archivo.isEmpty()) {
            body.add("foto2", new ByteArrayResource(archivo.getBytes()) {
                @Override
                public String getFilename() {
                    return archivo.getOriginalFilename();
                }
            });
        }

        HttpEntity<MultiValueMap<String, Object>> requestEntity =
            new HttpEntity<>(body, headers);

        ResponseEntity<ErrorMsg> response = restTemplate.exchange(
            URL,
            HttpMethod.POST,
            requestEntity,
            ErrorMsg.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Error al insertar artista: " + response.getStatusCode());
        }
    }

    /**
     * Elimina un artista por su identificador.
     *
     * @param id identificador del artista
     * @param jwtToken token JWT de autenticación
     */
    @Override
    public void borrarArtista(Integer id, String jwtToken) {
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
     * Obtiene la discografía de un artista.
     *
     * @param idArtista identificador del artista
     * @param jwtToken token JWT de autenticación
     * @return lista de discos del artista (puede estar vacía)
     */
    @Override
    public List<Disco> obtenerDiscografiaPorArtista(int idArtista, String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = URL + "/" + idArtista + "/discografia";

        try {
            ResponseEntity<Disco[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Disco[].class
            );

            Disco[] discos = response.getBody();
            return discos != null ? Arrays.asList(discos) : new ArrayList<>();

        } catch (HttpClientErrorException.NotFound e) {
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene un artista aleatorio del sistema.
     *
     * @param jwtToken token JWT de autenticación
     * @return artista seleccionado aleatoriamente, o null si no hay artistas
     */
    @Override
    public Artista obtenerArtistaAleatorio(String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Artista[]> response = restTemplate.exchange(
            URL,
            HttpMethod.GET,
            entity,
            Artista[].class
        );

        Artista[] artistas = response.getBody();
        if (artistas == null || artistas.length == 0) {
            return null;
        }

        Random random = new Random();
        int indice = random.nextInt(artistas.length);
        return artistas[indice];
    }
}
