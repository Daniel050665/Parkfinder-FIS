package com.parkfinder.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parkfinder.entities.TipoVehiculo;
import com.parkfinder.entities.Usuario;
import com.parkfinder.entities.Vehiculo;
import com.parkfinder.repositories.TipoVehiculoRepository;
import com.parkfinder.repositories.UsuarioRepository;
import com.parkfinder.repositories.VehiculoRepository;

/**
 * Servicio que gestiona el registro de vehiculos asociados
 * a los usuarios del sistema.
 *
 * @author Equipo ParkFinder
 * @version 1.0
 */
@Service
@Transactional
public class VehiculoService {
    
    private final VehiculoRepository vehiculoRepository;
    private final UsuarioRepository usuarioRepository;
    private final TipoVehiculoRepository tipoVehiculoRepository;

    public VehiculoService(
            VehiculoRepository vehiculoRepository,
            UsuarioRepository usuarioRepository,
            TipoVehiculoRepository tipoVehiculoRepository) {

        this.vehiculoRepository = vehiculoRepository;
        this.usuarioRepository = usuarioRepository;
        this.tipoVehiculoRepository = tipoVehiculoRepository;
    }

        /**
     * Registra un nuevo vehiculo asociado a un usuario.
     *
     * @param placa          placa del vehiculo
     * @param idUsuario      identificador del usuario propietario
     * @param idTipoVehiculo identificador del tipo de vehiculo
     * @return el vehiculo creado y persistido
     * @throws RuntimeException si el usuario o tipo de vehiculo no existen
     */
public Vehiculo registrarVehiculo(String placa, Long idUsuario, Long idTipoVehiculo){

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        TipoVehiculo tipoVehiculo = tipoVehiculoRepository.findById(idTipoVehiculo)
                .orElseThrow(() -> new RuntimeException("Tipo de vehiculo no encontrado"));

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPlaca(placa);
        vehiculo.setUsuario(usuario);
        vehiculo.setTipoVehiculo(tipoVehiculo);

        return vehiculoRepository.save(vehiculo);
    }
}
