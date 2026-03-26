package com.parkfinder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.parkfinder.entities.Usuario;
import com.parkfinder.repositories.UsuarioRepository;

import java.util.List;

@SpringBootApplication
public class ParkFinderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParkFinderApplication.class, args);
    }

    @Bean
    CommandLineRunner run(UsuarioRepository usuarioRepo) {
        return args -> {
        };
    }
}
