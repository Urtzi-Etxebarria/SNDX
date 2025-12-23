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

@Service
public class ProductorServicioImp implements ProductorServicio{
	
	private final RestTemplate restTemplate = new RestTemplate();
	private final String URL = "http://localhost:9090/api/productores";

	
	@Override
	public List<Productor> obtenerTodosProductores(String jwtToken) {
	    // [1] Crear un objeto para las cabeceras HTTP
	    HttpHeaders headers = new HttpHeaders();

	    // [2] Añadir el token JWT en la cabecera 'Authorization' con formato Bearer
	    headers.setBearerAuth(jwtToken); // Esto genera: Authorization: Bearer <token>

	    // [3] Crear la entidad HTTP que se enviará en la petición
	    //    En este caso, solo enviamos cabeceras (no hay body porque es un GET)
	    HttpEntity<String> entity = new HttpEntity<>(headers);

	    // [4] Hacer una petición GET a la URL especificada
	    //    Usamos restTemplate.exchange para enviar la solicitud y recibir una respuesta
	    ResponseEntity<Productor[]> response = restTemplate.exchange(
	        URL,                 // URL de la API REST externa (debería ser una constante o inyectada)
	        HttpMethod.GET,      // Tipo de solicitud HTTP: GET
	        entity,              // Entidad HTTP con las cabeceras (incluyendo el token)
	        Productor[].class       // Tipo de dato que esperamos recibir como respuesta (array de Cuadro)
	    );

	    // [5] Obtener el cuerpo de la respuesta (el array de objetos Cuadro)
	    Productor[] productor = response.getBody();

	    // [6] Convertir el array a una lista y devolverla
	    return Arrays.asList(productor);
	}

	
	@Override
	public Productor obtenerProductorPorId(int id, String jwtToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(jwtToken);

		HttpEntity<String> entity = new HttpEntity<>(headers);

		String URLID = URL + "/" + id;

		ResponseEntity<Productor> response = restTemplate.exchange(
			URLID,
			HttpMethod.GET,
			entity,
			Productor.class
		);

		return response.getBody();
	}

	@Override
	public void modificarProductor(Productor productor, String jwtToken) {
	    HttpHeaders headers = new HttpHeaders();
	    headers.setBearerAuth(jwtToken);
	    HttpEntity<Productor> entity = new HttpEntity<>(productor, headers);

	    restTemplate.exchange(
	        URL,
	        HttpMethod.PUT,
	        entity,
	        Void.class
	    );
	}


	@Override
	public void insertarProductor(Productor obj_productor, MultipartFile archivo, String jwtToken) throws IOException {
	    HttpHeaders headers = new HttpHeaders();
	    headers.setBearerAuth(jwtToken);
	    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

	    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

	    // Convertimos el productor a JSON y lo enviamos como recurso
	    ByteArrayResource productorResource = new ByteArrayResource(
	        new ObjectMapper().writeValueAsBytes(obj_productor)) {
	            @Override
	            public String getFilename() {
	                return "productor.json";
	            }
	        };
	    body.add("productor", productorResource);

	    // Adjuntamos la foto si existe (ajusta el nombre del campo al que espera tu REST)
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
	        URL,                // ⚠️ Asegúrate de que esta URL sea la del endpoint POST de productores
	        HttpMethod.POST,
	        requestEntity,
	        ErrorMsg.class
	    );

	    if (!response.getStatusCode().is2xxSuccessful()) {
	        throw new RuntimeException("Error al insertar productor: " + response.getStatusCode());
	    }
	}



	@Override
	public void borrarProductor(Integer id, String jwtToken) {
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
	public List<Disco> obtenerDiscografiaPorProductor(int idProductor, String jwtToken) {
	    HttpHeaders headers = new HttpHeaders();
	    headers.setBearerAuth(jwtToken);

	    HttpEntity<String> entity = new HttpEntity<>(headers);

	    String url = URL + "/" + idProductor + "/discografia";

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
	        // Si el artista no tiene discos, devolvemos lista vacía
	        return new ArrayList<>();
	    }
	}
	
	@Override
    public Productor obtenerProductorAleatorio(String jwtToken) {
        // 1️⃣ Crear cabeceras con el token JWT
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        // 2️⃣ Crear la entidad HTTP con las cabeceras
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // 3️⃣ Hacer la petición GET al endpoint que devuelve todos los artistas
        ResponseEntity<Productor[]> response = restTemplate.exchange(
                URL,                   // endpoint: ejemplo -> http://localhost:8081/api/artistas
                HttpMethod.GET,
                entity,
                Productor[].class
        );

        // 4️⃣ Obtener el array de artistas del cuerpo de la respuesta
        Productor[] productor = response.getBody();

        // 5️⃣ Si no hay artistas, devolver null
        if (productor == null || productor.length == 0) {
            return null;
        }

        // 6️⃣ Seleccionar uno aleatorio
        Random random = new Random();
        int indice = random.nextInt(productor.length);

        return productor[indice];
    }

}