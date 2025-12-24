package com.ipartek.componentes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración personalizada de Jackson para la aplicación.
 * <p>
 * Define un {@link ObjectMapper} como bean de Spring y registra
 * el módulo {@link JavaTimeModule} para permitir la serialización
 * y deserialización correcta de tipos de fecha y hora de Java 8+
 * (como {@link java.time.LocalDate}, {@link java.time.LocalDateTime}, etc.).
 * </p>
 */
@Configuration
public class JacksonConfig {

    /**
     * Crea y configura un {@link ObjectMapper} personalizado.
     * <p>
     * Este {@code ObjectMapper} incluye el {@link JavaTimeModule},
     * necesario para el soporte de la API {@code java.time}.
     * </p>
     *
     * @return un {@link ObjectMapper} configurado para manejar fechas y horas
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}
