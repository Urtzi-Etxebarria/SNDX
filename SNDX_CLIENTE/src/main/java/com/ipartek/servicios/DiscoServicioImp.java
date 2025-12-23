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

@Service
public class DiscoServicioImp implements DiscoServicio{
	
	private final RestTemplate restTemplate = new RestTemplate();
	private final String URL = "http://localhost:9090/api/discos";

	
	@Override
	public List<Disco> obtenerTodosDiscos(String jwtToken) {
	    // [1] Crear un objeto para las cabeceras HTTP
	    HttpHeaders headers = new HttpHeaders();

	    // [2] Añadir el token JWT en la cabecera 'Authorization' con formato Bearer
	    headers.setBearerAuth(jwtToken); // Esto genera: Authorization: Bearer <token>

	    // [3] Crear la entidad HTTP que se enviará en la petición
	    //    En este caso, solo enviamos cabeceras (no hay body porque es un GET)
	    HttpEntity<String> entity = new HttpEntity<>(headers);

	    // [4] Hacer una petición GET a la URL especificada
	    //    Usamos restTemplate.exchange para enviar la solicitud y recibir una respuesta
	    ResponseEntity<Disco[]> response = restTemplate.exchange(
	        URL,                 // URL de la API REST externa (debería ser una constante o inyectada)
	        HttpMethod.GET,      // Tipo de solicitud HTTP: GET
	        entity,              // Entidad HTTP con las cabeceras (incluyendo el token)
	        Disco[].class       // Tipo de dato que esperamos recibir como respuesta (array de Cuadro)
	    );

	    // [5] Obtener el cuerpo de la respuesta (el array de objetos Cuadro)
	    Disco[] disco = response.getBody();

	    // [6] Convertir el array a una lista y devolverla
	    return Arrays.asList(disco);
	}

	
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

	@Override
	public void modificarDisco(Disco disco, String jwtToken) {
	    HttpHeaders headers = new HttpHeaders();
	    headers.setBearerAuth(jwtToken);
	    HttpEntity<Disco> entity = new HttpEntity<>(disco, headers);

	    restTemplate.exchange(
	        URL,
	        HttpMethod.PUT,
	        entity,
	        Void.class
	    );
	}

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
	        throw new RuntimeException("Error al insertar artista: " + response.getStatusCode());
	    }
    
	}

	
	@Override
	public void borrarDisco(Integer id, String jwtToken) {
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
    public Disco obtenerDiscoAleatorio(String jwtToken) {
        // 1️⃣ Crear cabeceras con el token JWT
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        // 2️⃣ Crear la entidad HTTP con las cabeceras
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // 3️⃣ Hacer la petición GET al endpoint que devuelve todos los artistas
        ResponseEntity<Disco[]> response = restTemplate.exchange(
                URL,                   // endpoint: ejemplo -> http://localhost:8081/api/artistas
                HttpMethod.GET,
                entity,
                Disco[].class
        );

        // 4️⃣ Obtener el array de artistas del cuerpo de la respuesta
        Disco[] disco = response.getBody();

        // 5️⃣ Si no hay artistas, devolver null
        if (disco == null || disco.length == 0) {
            return null;
        }

        // 6️⃣ Seleccionar uno aleatorio
        Random random = new Random();
        int indice = random.nextInt(disco.length);

        return disco[indice];
    }

}
