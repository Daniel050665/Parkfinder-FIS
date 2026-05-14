package com.parkfinder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.parkfinder.entities.Usuario;
import com.parkfinder.repositories.UsuarioRepository;

import java.util.List;

@SpringBootApplication
/**
 * Clase principal de configuracion de Spring Boot.
 * Define el punto de entrada del contexto de Spring y habilita
 * la auto-configuracion de JPA, H2 y componentes del sistema.
 *
 * @author Equipo ParkFinder
 * @version 1.0
 */
public class   ParkFinderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParkFinderApplication.class, args);
    }

}