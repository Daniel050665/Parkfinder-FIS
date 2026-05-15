package com.parkfinder.services;

import com.parkfinder.entities.Beneficio;
import com.parkfinder.entities.Usuario;
import com.parkfinder.repositories.BeneficioRepository;
import com.parkfinder.repositories.UsuarioRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio que gestiona la logica del sistema de fidelizacion.
 * Genera beneficios automaticamente cada 3 usos pagados y
 * permite su aplicacion como descuento.
 *
 * @author Equipo ParkFinder
 * @version 1.0
 */
@Service
@Transactional
public class BeneficioService {

    private final BeneficioRepository beneficioRepository;
    private final UsuarioRepository usuarioRepository;

    public BeneficioService(BeneficioRepository beneficioRepository,
                            UsuarioRepository usuarioRepository) {
        this.beneficioRepository = beneficioRepository;
        this.usuarioRepository = usuarioRepository;
    }

        /**
     * Crea manualmente un beneficio de fidelizacion para un usuario.
     *
     * @param idUsuario identificador del usuario beneficiario
     * @return el beneficio creado y disponible para uso
     * @throws RuntimeException si el usuario no existe
     */
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

        /**
     * Obtiene todos los beneficios asociados a un usuario.
     *
     * @param idUsuario identificador del usuario
     * @return lista de beneficios del usuario
     */
public List<Beneficio> obtenerBeneficios(Long idUsuario) {

        return beneficioRepository.findByUsuarioIdUsuario(idUsuario);
    }

        /**
     * Marca un beneficio como utilizado.
     *
     * @param idBeneficio identificador del beneficio a usar
     * @return el beneficio actualizado con fecha de uso
     * @throws RuntimeException si el beneficio no existe o ya fue usado
     */
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

        /**
     * Genera automaticamente un beneficio si el usuario ha completado
     * un multiplo de 3 usos pagados.
     *
     * @param usuario el usuario a evaluar para generacion de beneficio
     */
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