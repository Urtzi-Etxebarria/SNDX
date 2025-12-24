package com.ipartek.repositorio;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ipartek.modelo.Usuario;

/**
 * Repositorio Spring Data JPA para la entidad {@link Usuario}.
 * <p>
 * Proporciona operaciones CRUD básicas y métodos personalizados
 * para acceder a los datos de los usuarios almacenados en la base de datos.
 * </p>
 */
@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Integer> {

    /**
     * Busca un usuario por su nombre.
     *
     * @param name nombre del usuario a buscar
     * @return un {@link Optional} que contiene el usuario si existe,
     *         o vacío si no se encuentra
     */
    Optional<Usuario> findByName(String name);
}

