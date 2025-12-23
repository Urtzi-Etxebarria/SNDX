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

@Service
public class ArtistaServicioImp implements ArtistaServicio{
	
	private final RestTemplate restTemplate = new RestTemplate();
	private final String URL = "http://localhost:9090/api/artistas";

	
	@Override
	public List<Artista> obtenerTodosArtistas(String jwtToken) {
	    // [1] Crear un objeto para las cabeceras HTTP
	    HttpHeaders headers = new HttpHeaders();

	    // [2] Añadir el token JWT en la cabecera 'Authorization' con formato Bearer
	    headers.setBearerAuth(jwtToken); // Esto genera: Authorization: Bearer <token>

	    // [3] Crear la entidad HTTP que se enviará en la petición
	    //    En este caso, solo enviamos cabeceras (no hay body porque es un GET)
	    HttpEntity<String> entity = new HttpEntity<>(headers);

	    // [4] Hacer una petición GET a la URL especificada
	    //    Usamos restTemplate.exchange para enviar la solicitud y recibir una respuesta
	    ResponseEntity<Artista[]> response = restTemplate.exchange(
	        URL,                 // URL de la API REST externa (debería ser una constante o inyectada)
	        HttpMethod.GET,      // Tipo de solicitud HTTP: GET
	        entity,              // Entidad HTTP con las cabeceras (incluyendo el token)
	        Artista[].class       // Tipo de dato que esperamos recibir como respuesta (array de Cuadro)
	    );

	    // [5] Obtener el cuerpo de la respuesta (el array de objetos Cuadro)
	    Artista[] artista = response.getBody();

	    // [6] Convertir el array a una lista y devolverla
	    return Arrays.asList(artista);
	}

	
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


	@Override
	public void insertarArtista(Artista obj_artista, MultipartFile archivo, String jwtToken) throws IOException {
	    HttpHeaders headers = new HttpHeaders();
	    headers.setBearerAuth(jwtToken);
	    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

	    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
	    // Convertimos artista a JSON y lo enviamos como recurso
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
	        // Si el artista no tiene discos, devolvemos lista vacía
	        return new ArrayList<>();
	    }
	}
	
	@Override
    public Artista obtenerArtistaAleatorio(String jwtToken) {
        // 1️⃣ Crear cabeceras con el token JWT
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        // 2️⃣ Crear la entidad HTTP con las cabeceras
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // 3️⃣ Hacer la petición GET al endpoint que devuelve todos los artistas
        ResponseEntity<Artista[]> response = restTemplate.exchange(
                URL,                   // endpoint: ejemplo -> http://localhost:8081/api/artistas
                HttpMethod.GET,
                entity,
                Artista[].class
        );

        // 4️⃣ Obtener el array de artistas del cuerpo de la respuesta
        Artista[] artistas = response.getBody();

        // 5️⃣ Si no hay artistas, devolver null
        if (artistas == null || artistas.length == 0) {
            return null;
        }

        // 6️⃣ Seleccionar uno aleatorio
        Random random = new Random();
        int indice = random.nextInt(artistas.length);

        return artistas[indice];
    }
	    
}
