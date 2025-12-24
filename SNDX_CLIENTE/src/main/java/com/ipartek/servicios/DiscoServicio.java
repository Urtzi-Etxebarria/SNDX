package com.ipartek.servicios;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.ipartek.pojos.Disco;

/**
 * Interfaz de servicio para la gestión de discos.
 * <p>
 * Define las operaciones disponibles para la obtención, modificación,
 * inserción y eliminación de discos, así como la selección de un disco aleatorio.
 * Todas las operaciones requieren un token JWT válido para autenticación.
 * </p>
 */
public interface DiscoServicio {

    /**
     * Obtiene la lista de todos los discos del sistema.
     *
     * @param jwtToken token JWT de autenticación
     * @return lista de discos
     */
    List<Disco> obtenerTodosDiscos(String jwtToken);

    /**
     * Obtiene un disco por su identificador.
     *
     * @param id identificador del disco
     * @param jwtToken token JWT de autenticación
     * @return disco correspondiente al ID
     */
    Disco obtenerDiscoPorId(int id, String jwtToken);

    /**
     * Obtiene un disco aleatorio del sistema.
     *
     * @param jwtToken token JWT de autenticación
     * @return disco seleccionado aleatoriamente
     */
    Disco obtenerDiscoAleatorio(String jwtToken);

    /**
     * Modifica los datos de un disco existente.
     *
     * @param disco disco con los datos actualizados
     * @param jwtToken token JWT de autenticación
     */
    void modificarDisco(Disco disco, String jwtToken);

    /**
     * Inserta un nuevo disco en el sistema.
     *
     * @param disco disco a insertar
     * @param archivo archivo de imagen del disco (opcional)
     * @param jwtToken token JWT de autenticación
     * @throws IOException si ocurre un error al procesar el archivo
     */
    void insertarDisco(Disco disco, MultipartFile archivo, String jwtToken) throws IOException;

    /**
     * Elimina un disco por su identificador.
     *
     * @param id identificador del disco a eliminar
     * @param jwtToken token JWT de autenticación
     */
    void borrarDisco(Integer id, String jwtToken);
}
