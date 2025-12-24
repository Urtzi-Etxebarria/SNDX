package com.ipartek.servicios;

import java.util.List;
import com.ipartek.pojos.Disco;
import com.ipartek.pojos.Genero;

/**
 * Interfaz de servicio para la gestión de géneros musicales.
 * <p>
 * Proporciona métodos para obtener, modificar, insertar y eliminar géneros,
 * así como para obtener los discos asociados a un género específico.
 * Todas las operaciones requieren un token JWT válido.
 * </p>
 */
public interface GeneroServicio {

    /**
     * Obtiene todos los géneros musicales del sistema.
     *
     * @param jwtToken token JWT de autenticación
     * @return lista de géneros
     */
    List<Genero> obtenerTodosGeneros(String jwtToken);

    /**
     * Obtiene todos los discos asociados a un género.
     *
     * @param idGenero identificador del género
     * @param jwtToken token JWT de autenticación
     * @return lista de discos asociados al género
     */
    List<Disco> obtenerDiscosPorGenero(int idGenero, String jwtToken);

    /**
     * Obtiene un género por su identificador.
     *
     * @param id identificador del género
     * @param jwtToken token JWT de autenticación
     * @return género correspondiente al ID
     */
    Genero obtenerGeneroPorId(int id, String jwtToken);

    /**
     * Modifica los datos de un género existente.
     *
     * @param genero género con los datos actualizados
     * @param jwtToken token JWT de autenticación
     */
    void modificarGenero(Genero genero, String jwtToken);

    /**
     * Inserta un nuevo género en el sistema.
     *
     * @param genero género a insertar
     * @param jwtToken token JWT de autenticación
     */
    void insertarGenero(Genero genero, String jwtToken);

    /**
     * Elimina un género por su identificador.
     *
     * @param id identificador del género a eliminar
     * @param jwtToken token JWT de autenticación
     */
    void borrarGenero(Integer id, String jwtToken);
}
