package com.parkfinder.services;

import com.parkfinder.entities.Beneficio;
import com.parkfinder.entities.Usuario;
import com.parkfinder.repositories.BeneficioRepository;
import com.parkfinder.repositories.UsuarioRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BeneficioService {

    private final BeneficioRepository beneficioRepository;
    private final UsuarioRepository usuarioRepository;

    public BeneficioService(BeneficioRepository beneficioRepository,
                            UsuarioRepository usuarioRepository) {
        this.beneficioRepository = beneficioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Beneficio crearBeneficio(Long idUsuario) {

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Beneficio beneficio = new Beneficio();
        beneficio.setUsuario(usuario);
        beneficio.setBeneficioDisponible(true);
        beneficio.setFechaGeneracion(LocalDate.now());
        beneficio.setUsosAcumulados(usuario.getUsosAcumulados());

        return beneficioRepository.save(beneficio);
    }

    public List<Beneficio> obtenerBeneficios(Long idUsuario) {

        return beneficioRepository.findByUsuarioIdUsuario(idUsuario);
    }

    public Beneficio usarBeneficio(Long idBeneficio) {

        Beneficio beneficio = beneficioRepository.findById(idBeneficio)
                .orElseThrow(() -> new RuntimeException("Beneficio no encontrado"));

        if (Boolean.FALSE.equals(beneficio.getBeneficioDisponible())) {
            throw new RuntimeException("El beneficio ya fue usado");
        }

        beneficio.setBeneficioDisponible(false);
        beneficio.setFechaUso(LocalDate.now());

        return beneficioRepository.save(beneficio);
    }

    public void generarBeneficioSiAplica(Usuario usuario) {

        int usos = usuario.getUsosAcumulados();

        if (usos > 0 && usos % 3 == 0) {

            Beneficio beneficio = new Beneficio();
            beneficio.setUsuario(usuario);
            beneficio.setBeneficioDisponible(true);
            beneficio.setFechaGeneracion(LocalDate.now());
            beneficio.setUsosAcumulados(usos);

            beneficioRepository.save(beneficio);
        }
    }
}