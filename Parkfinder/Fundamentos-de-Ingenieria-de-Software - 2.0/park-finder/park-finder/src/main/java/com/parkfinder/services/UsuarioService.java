package com.parkfinder.services;

import com.parkfinder.entities.Usuario;
import com.parkfinder.entities.Vehiculo;
import com.parkfinder.entities.TipoVehiculo;

import com.parkfinder.repositories.UsuarioRepository;
import com.parkfinder.repositories.VehiculoRepository;
import com.parkfinder.repositories.TipoVehiculoRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final VehiculoRepository vehiculoRepository;
    private final TipoVehiculoRepository tipoVehiculoRepository;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          VehiculoRepository vehiculoRepository,
                          TipoVehiculoRepository tipoVehiculoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.tipoVehiculoRepository = tipoVehiculoRepository;
    }

    public Usuario registrarUsuario(String nombre,
                                    String correo,
                                    String contrasena,
                                    String placa,
                                    Long idTipoVehiculo) {

        Optional<Usuario> usuarioExistente = usuarioRepository.findByCorreo(correo);

        if (usuarioExistente.isPresent()) {
            throw new RuntimeException("El correo ya está registrado");
        }

        TipoVehiculo tipoVehiculo = tipoVehiculoRepository.findById(idTipoVehiculo)
                .orElseThrow(() -> new RuntimeException("Tipo de vehículo no encontrado"));

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setCorreo(correo);
        usuario.setContrasena(contrasena);
        usuario.setFechaRegistro(LocalDate.now());
        usuario.setEstadoCuenta(true);
        usuario.setUsosAcumulados(0);

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPlaca(placa);
        vehiculo.setUsuario(usuarioGuardado);
        vehiculo.setTipoVehiculo(tipoVehiculo);

        vehiculoRepository.save(vehiculo);

        return usuarioGuardado;
    }

    public Usuario iniciarSesion(String correo, String contrasena){

        Optional<Usuario> usuarioOptional = usuarioRepository.findByCorreo(correo);

        if(usuarioOptional.isEmpty()){
            throw new RuntimeException("Usuario no encontrado");
        }

        Usuario usuario = usuarioOptional.get();

        if(!usuario.getContrasena().equals(contrasena)){
            throw new RuntimeException("Contraseña incorrecta");
        }

        return usuario;
    }
}