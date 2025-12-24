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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipartek.pojos.Disco;
import com.ipartek.pojos.Discografica;
import com.ipartek.pojos.ErrorMsg;

/**
 * Implementación del servicio {@link DiscograficaServicio} que gestiona
 * operaciones sobre discográficas mediante comunicación con un API REST externa.
 * <p>
 * Todas las operaciones requieren un token JWT válido para autenticación.
 * </p>
 */
@Service
public class DiscograficaServicioImp implements DiscograficaServicio {

    /**
     * Cliente REST para realizar llamadas HTTP al API externo.
     */
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * URL base del endpoint de discográficas.
     */
    private final String URL = "http://localhost:9090/api/discograficas";

    /**
     * Obtiene la lista de todas las discográficas.
     *
     * @param jwtToken token JWT de autenticación
     * @return lista de discográficas
     */
    @Override
    public List<Discografica> obtenerTodasDiscograficas(String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Discografica[]> response = restTemplate.exchange(
            URL,
            HttpMethod.GET,
            entity,
            Discografica[].class
        );

        Discografica[] discografica = response.getBody();
        return Arrays.asList(discografica);
    }

    /**
     * Obtiene una discográfica por su identificador.
     *
     * @param id identificador de la discográfica
     * @param jwtToken token JWT de autenticación
     * @return discográfica correspondiente al ID
     */
    @Override
    public Discografica obtenerDiscograficaPorId(int id, String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        String URLID = URL + "/" + id;

        ResponseEntity<Discografica> response = restTemplate.exchange(
            URLID,
            HttpMethod.GET,
            entity,
            Discografica.class
        );

        return response.getBody();
    }

    /**
     * Modifica los datos de una discográfica existente.
     *
     * @param discografica discográfica con los datos actualizados
     * @param jwtToken token JWT de autenticación
     */
    @Override
    public void modificarDiscografica(Discografica discografica, String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<Discografica> entity = new HttpEntity<>(discografica, headers);

        restTemplate.exchange(
            URL,
            HttpMethod.PUT,
            entity,
            Void.class
        );
    }

    /**
     * Inserta una nueva discográfica en el sistema.
     *
     * @param obj_discografica discográfica a insertar
     * @param archivo archivo de imagen de la discográfica (opcional)
     * @param jwtToken token JWT de autenticación
     * @throws IOException si ocurre un error al procesar el archivo
     */
    @Override
    public void insertarDiscografica(Discografica obj_discografica, MultipartFile archivo, String jwtToken) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        ByteArrayResource discograficaResource = new ByteArrayResource(
            new ObjectMapper().writeValueAsBytes(obj_discografica)) {
                @Override
                public String getFilename() {
                    return "discografica.json";
                }
            };
        body.add("discografica", discograficaResource);

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
            throw new RuntimeException("Error al insertar discográfica: " + response.getStatusCode());
        }
    }

    /**
     * Elimina una discográfica por su identificador.
     *
     * @param id identificador de la discográfica a eliminar
     * @param jwtToken token JWT de autenticación
     */
    @Override
    public void borrarDiscografica(Integer id, String jwtToken) {
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
     * Obtiene la discografía asociada a una discográfica concreta.
     *
     * @param idDiscografica identificador de la discográfica
     * @param jwtToken token JWT de autenticación
     * @return lista de discos asociados a la discográfica
     */
    @Override
    public List<Disco> obtenerDiscografiaPorDiscografica(int idDiscografica, String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = URL + "/" + idDiscografica + "/discografia";

        ResponseEntity<Disco[]> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            Disco[].class
        );

        Disco[] discos = response.getBody();
        return discos != null ? Arrays.asList(discos) : new ArrayList<>();
    }

    /**
     * Obtiene una discográfica aleatoria del sistema.
     *
     * @param jwtToken token JWT de autenticación
     * @return discográfica seleccionada aleatoriamente, o null si no hay discográficas
     */
    @Override
    public Discografica obtenerDiscograficaAleatorio(String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Discografica[]> response = restTemplate.exchange(
                URL,
                HttpMethod.GET,
                entity,
                Discografica[].class
        );

        Discografica[] discografica = response.getBody();
        if (discografica == null || discografica.length == 0) {
            return null;
        }

        Random random = new Random();
        int indice = random.nextInt(discografica.length);
        return discografica[indice];
    }
}
