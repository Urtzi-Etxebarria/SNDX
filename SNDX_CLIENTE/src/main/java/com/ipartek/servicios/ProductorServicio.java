package com.ipartek.servicios;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.ipartek.pojos.Disco;
import com.ipartek.pojos.Productor;

/**
 * Interfaz de servicio para la gestión de productores musicales.
 * <p>
 * Define las operaciones CRUD para productores, así como métodos para obtener
 * la discografía asociada a un productor y seleccionar un productor aleatorio.
 * Todas las operaciones requieren un token JWT válido para autenticación.
 * </p>
 */
public interface ProductorServicio {

    /**
     * Obtiene la lista de todos los productores disponibles.
     *
     * @param jwtToken Token JWT para autenticación.
     * @return Lista de productores.
     */
    List<Productor> obtenerTodosProductores(String jwtToken);

    /**
     * Obtiene la discografía asociada a un productor específico.
     *
     * @param idProductor ID del productor.
     * @param jwtToken Token JWT para autenticación.
     * @return Lista de discos asociados al productor.
     */
    List<Disco> obtenerDiscografiaPorProductor(int idProductor, String jwtToken);

    /**
     * Obtiene un productor por su ID.
     *
     * @param id ID del productor.
     * @param jwtToken Token JWT para autenticación.
     * @return Productor correspondiente al ID proporcionado.
     */
    Productor obtenerProductorPorId(int id, String jwtToken);

    /**
     * Obtiene un productor de forma aleatoria.
     *
     * @param jwtToken Token JWT para autenticación.
     * @return Productor seleccionado aleatoriamente.
     */
    Productor obtenerProductorAleatorio(String jwtToken);

    /**
     * Modifica los datos de un productor existente.
     *
     * @param obj_productor Productor con los datos actualizados.
     * @param jwtToken Token JWT para autenticación.
     */
    void modificarProductor(Productor obj_productor, String jwtToken);

    /**
     * Inserta un nuevo productor en el sistema, opcionalmente con su foto.
     *
     * @param obj_productor Productor a insertar.
     * @param archivo Archivo de imagen opcional.
     * @param jwtToken Token JWT para autenticación.
     * @throws IOException Si ocurre un error al procesar el archivo.
     */
    void insertarProductor(Productor obj_productor, MultipartFile archivo, String jwtToken) throws IOException;

    /**
     * Elimina un productor por su ID.
     *
     * @param id ID del productor a eliminar.
     * @param jwtToken Token JWT para autenticación.
     */
    void borrarProductor(Integer id, String jwtToken);
}
