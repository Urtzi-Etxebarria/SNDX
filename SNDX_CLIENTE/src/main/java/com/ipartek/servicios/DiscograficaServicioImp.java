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

@Service
public class DiscograficaServicioImp implements DiscograficaServicio{
	
	private final RestTemplate restTemplate = new RestTemplate();
	private final String URL = "http://localhost:9090/api/discograficas";
	
	
	@Override
	public List<Discografica> obtenerTodasDiscograficas(String jwtToken) {
	    // [1] Crear un objeto para las cabeceras HTTP
	    HttpHeaders headers = new HttpHeaders();

	    // [2] Añadir el token JWT en la cabecera 'Authorization' con formato Bearer
	    headers.setBearerAuth(jwtToken); // Esto genera: Authorization: Bearer <token>

	    // [3] Crear la entidad HTTP que se enviará en la petición
	    //    En este caso, solo enviamos cabeceras (no hay body porque es un GET)
	    HttpEntity<String> entity = new HttpEntity<>(headers);

	    // [4] Hacer una petición GET a la URL especificada
	    //    Usamos restTemplate.exchange para enviar la solicitud y recibir una respuesta
	    ResponseEntity<Discografica[]> response = restTemplate.exchange(
	        URL,                 // URL de la API REST externa (debería ser una constante o inyectada)
	        HttpMethod.GET,      // Tipo de solicitud HTTP: GET
	        entity,              // Entidad HTTP con las cabeceras (incluyendo el token)
	        Discografica[].class       // Tipo de dato que esperamos recibir como respuesta (array de Cuadro)
	    );

	    // [5] Obtener el cuerpo de la respuesta (el array de objetos Cuadro)
	    Discografica[] discografica = response.getBody();

	    // [6] Convertir el array a una lista y devolverla
	    return Arrays.asList(discografica);
	}

	
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


	@Override
	public void insertarDiscografica(Discografica obj_discografica, MultipartFile archivo, String jwtToken) throws IOException {
	    HttpHeaders headers = new HttpHeaders();
	    headers.setBearerAuth(jwtToken);
	    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

	    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

	    // Convertimos la discográfica a JSON y la enviamos como recurso
	    ByteArrayResource discograficaResource = new ByteArrayResource(
	        new ObjectMapper().writeValueAsBytes(obj_discografica)) {
	            @Override
	            public String getFilename() {
	                return "discografica.json";
	            }
	        };
	    body.add("discografica", discograficaResource);

	    // Adjuntamos el logo si existe
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
	        URL,              // Asegúrate de que este sea el endpoint correcto para insertar discográfica
	        HttpMethod.POST,
	        requestEntity,
	        ErrorMsg.class
	    );

	    if (!response.getStatusCode().is2xxSuccessful()) {
	        throw new RuntimeException("Error al insertar discográfica: " + response.getStatusCode());
	    }
	}



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
	
	@Override
    public Discografica obtenerDiscograficaAleatorio(String jwtToken) {
        // 1️⃣ Crear cabeceras con el token JWT
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        // 2️⃣ Crear la entidad HTTP con las cabeceras
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // 3️⃣ Hacer la petición GET al endpoint que devuelve todos los artistas
        ResponseEntity<Discografica[]> response = restTemplate.exchange(
                URL,                   // endpoint: ejemplo -> http://localhost:8081/api/artistas
                HttpMethod.GET,
                entity,
                Discografica[].class
        );

        // 4️⃣ Obtener el array de artistas del cuerpo de la respuesta
        Discografica[] discografica = response.getBody();

        // 5️⃣ Si no hay artistas, devolver null
        if (discografica == null || discografica.length == 0) {
            return null;
        }

        // 6️⃣ Seleccionar uno aleatorio
        Random random = new Random();
        int indice = random.nextInt(discografica.length);

        return discografica[indice];
    }

}