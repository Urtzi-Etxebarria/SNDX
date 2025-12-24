package com.ipartek.servicios;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.ipartek.pojos.Artista;
import com.ipartek.pojos.Disco;

/**
 * Interfaz de servicio para la gestión de artistas musicales.
 * <p>
 * Define las operaciones disponibles para la obtención, modificación,
 * inserción y eliminación de artistas, así como para acceder a su discografía.
 * Todas las operaciones requieren un token JWT para autenticación.
 * </p>
 */
public interface ArtistaServicio {

    /**
     * Obtiene la lista de todos los artistas.
     *
     * @param jwtToken token JWT de autenticación
     * @return lista de artistas
     */
    List<Artista> obtenerTodosArtistas(String jwtToken);

    /**
     * Obtiene la discografía de un artista concreto.
     *
     * @param id identificador del artista
     * @param jwtToken token JWT de autenticación
     * @return lista de discos del artista
     */
    List<Disco> obtenerDiscografiaPorArtista(int id, String jwtToken);

    /**
     * Obtiene un artista por su identificador.
     *
     * @param id identificador del artista
     * @param jwtToken token JWT de autenticación
     * @return objeto {@link Artista} correspondiente al ID
     */
    Artista obtenerArtistaPorId(int id, String jwtToken);

    /**
     * Modifica los datos de un artista existente.
     *
     * @param obj_artista artista con los datos actualizados
     * @param jwtToken token JWT de autenticación
     */
    void modificarArtista(Artista obj_artista, String jwtToken);

    /**
     * Inserta un nuevo artista en el sistema.
     *
     * @param obj_artista artista a insertar
     * @param archivo archivo de imagen del artista (opcional)
     * @param jwtToken token JWT de autenticación
     * @throws IOException si ocurre un error al procesar el archivo
     */
    void insertarArtista(Artista obj_artista, MultipartFile archivo, String jwtToken) throws IOException;

    /**
     * Elimina un artista por su identificador.
     *
     * @param id identificador del artista a eliminar
     * @param jwtToken token JWT de autenticación
     */
    void borrarArtista(Integer id, String jwtToken);

    /**
     * Obtiene un artista aleatorio del sistema.
     *
     * @param jwtToken token JWT de autenticación
     * @return artista seleccionado aleatoriamente
     */
    Artista obtenerArtistaAleatorio(String jwtToken);
}
