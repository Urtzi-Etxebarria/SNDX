package com.ipartek.servicios;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.ipartek.pojos.Disco;
import com.ipartek.pojos.Discografica;

/**
 * Interfaz de servicio para la gestión de discográficas.
 * <p>
 * Define las operaciones disponibles para la obtención, modificación,
 * inserción y eliminación de discográficas, así como para acceder a
 * la discografía asociada a cada una de ellas.
 * Todas las operaciones requieren un token JWT para autenticación.
 * </p>
 */
public interface DiscograficaServicio {

    /**
     * Obtiene la lista de todas las discográficas.
     *
     * @param jwtToken token JWT de autenticación
     * @return lista de discográficas
     */
    List<Discografica> obtenerTodasDiscograficas(String jwtToken);

    /**
     * Obtiene la discografía de una discográfica concreta.
     *
     * @param idDiscografica identificador de la discográfica
     * @param jwtToken token JWT de autenticación
     * @return lista de discos de la discográfica
     */
    List<Disco> obtenerDiscografiaPorDiscografica(int idDiscografica, String jwtToken);

    /**
     * Obtiene una discográfica por su identificador.
     *
     * @param id identificador de la discográfica
     * @param jwtToken token JWT de autenticación
     * @return discográfica correspondiente al ID
     */
    Discografica obtenerDiscograficaPorId(int id, String jwtToken);

    /**
     * Obtiene una discográfica aleatoria del sistema.
     *
     * @param jwtToken token JWT de autenticación
     * @return discográfica seleccionada aleatoriamente
     */
    Discografica obtenerDiscograficaAleatorio(String jwtToken);

    /**
     * Modifica los datos de una discográfica existente.
     *
     * @param obj_discografica discográfica con los datos actualizados
     * @param jwtToken token JWT de autenticación
     */
    void modificarDiscografica(Discografica obj_discografica, String jwtToken);

    /**
     * Inserta una nueva discográfica en el sistema.
     *
     * @param obj_discografica discográfica a insertar
     * @param archivo archivo de imagen de la discográfica (opcional)
     * @param jwtToken token JWT de autenticación
     * @throws IOException si ocurre un error al procesar el archivo
     */
    void insertarDiscografica(Discografica obj_discografica, MultipartFile archivo, String jwtToken) throws IOException;

    /**
     * Elimina una discográfica por su identificador.
     *
     * @param id identificador de la discográfica a eliminar
     * @param jwtToken token JWT de autenticación
     */
    void borrarDiscografica(Integer id, String jwtToken);
}
