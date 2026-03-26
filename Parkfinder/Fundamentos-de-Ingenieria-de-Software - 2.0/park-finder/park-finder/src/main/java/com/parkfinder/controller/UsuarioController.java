package com.parkfinder.controller;

import com.parkfinder.entities.Usuario;
import com.parkfinder.services.UsuarioService;
import com.parkfinder.dtos.UsuarioDto;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registro")
    public Usuario registrar(@RequestBody UsuarioDto usuarioDto){

        return usuarioService.registrarUsuario(
                usuarioDto.getNombre(),
                usuarioDto.getCorreo(),
                usuarioDto.getContrasena(),
                usuarioDto.getPlaca(),
                usuarioDto.getIdTipoVehiculo()
        );
    }

    @PostMapping("/login")
    public Usuario login(@RequestBody Usuario usuario){

        return usuarioService.iniciarSesion(
                usuario.getCorreo(),
                usuario.getContrasena()
        );
    }

}