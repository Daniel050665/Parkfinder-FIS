package com.parkfinder.services;

import com.parkfinder.entities.Usuario;
import com.parkfinder.entities.Vehiculo;
import com.parkfinder.entities.TipoVehiculo;

import com.parkfinder.repositories.UsuarioRepository;
import com.parkfinder.repositories.VehiculoRepository;
import com.parkfinder.repositories.TipoVehiculoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Servicio que gestiona la logica de negocio para el registro
 * e inicio de sesion de usuarios en el sistema ParkFinder.
 *
 * @author Equipo ParkFinder
 * @version 1.0
 */
@Service
@Transactional
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

        /**
     * Registra un nuevo usuario en el sistema junto con su vehiculo.
     *
     * @param nombre         nombre completo del usuario
     * @param correo         correo electronico (debe ser unico)
     * @param contrasena     contrasena en texto plano
     * @param placa          placa del vehiculo a registrar
     * @param idTipoVehiculo identificador del tipo de vehiculo
     * @return el usuario creado y persistido
     * @throws RuntimeException si el correo ya esta registrado o el tipo de vehiculo no existe
     */
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
        System.out.println("[UsuarioService] Usuario registrado: " + usuarioGuardado.getCorreo() + " (ID=" + usuarioGuardado.getIdUsuario() + ")");

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPlaca(placa);
        vehiculo.setUsuario(usuarioGuardado);
        vehiculo.setTipoVehiculo(tipoVehiculo);

        vehiculoRepository.save(vehiculo);
        System.out.println("[UsuarioService] Vehiculo registrado: " + placa);

        return usuarioGuardado;
    }

        /**
     * Autentica un usuario mediante correo y contrasena.
     *
     * @param correo     correo electronico del usuario
     * @param contrasena contrasena en texto plano
     * @return el usuario autenticado
     * @throws RuntimeException si el usuario no existe o la contrasena es incorrecta
     */
public Usuario iniciarSesion(String correo, String contrasena){

        Optional<Usuario> usuarioOptional = usuarioRepository.findByCorreo(correo);

        if(usuarioOptional.isEmpty()){
            throw new RuntimeException("Usuario no encontrado");
        }

        Usuario usuario = usuarioOptional.get();

        if(!usuario.getContrasena().equals(contrasena)){
            throw new RuntimeException("Contrasena incorrecta");
        }

        System.out.println("[UsuarioService] Login exitoso: " + usuario.getCorreo() + " (ID=" + usuario.getIdUsuario() + ")");
        return usuario;
    }
}