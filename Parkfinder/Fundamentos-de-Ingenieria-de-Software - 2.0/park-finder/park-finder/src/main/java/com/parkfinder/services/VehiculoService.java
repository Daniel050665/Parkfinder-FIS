package com.parkfinder.services;

import org.springframework.stereotype.Service;

import com.parkfinder.entities.TipoVehiculo;
import com.parkfinder.entities.Usuario;
import com.parkfinder.entities.Vehiculo;
import com.parkfinder.repositories.TipoVehiculoRepository;
import com.parkfinder.repositories.UsuarioRepository;
import com.parkfinder.repositories.VehiculoRepository;

@Service
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
