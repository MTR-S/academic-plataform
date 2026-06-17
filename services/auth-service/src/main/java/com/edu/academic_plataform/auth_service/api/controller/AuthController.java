package com.edu.academic_plataform.auth_service.api.controller;

import com.edu.academic_plataform.auth_service.api.dto.UsuarioCadastroDTO;
import com.edu.academic_plataform.auth_service.api.dto.UsuarioLoginDTO;
import com.edu.academic_plataform.auth_service.database.UsuarioEntity;
import com.edu.academic_plataform.auth_service.domain.enums.TipoUsuario;
import com.edu.academic_plataform.auth_service.domain.model.Usuario;
import com.edu.academic_plataform.auth_service.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/academic-plataform/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody UsuarioLoginDTO dto) {
        String token = usuarioService.autenticar(dto);

        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/register")
    public ResponseEntity<Usuario> registrar(@Valid @RequestBody UsuarioCadastroDTO dto) {

        TipoUsuario tipoDoUsuario = TipoUsuario.valueOf(dto.tipo().toUpperCase());

        Usuario novoUsuario = new Usuario(
                null,
                dto.nome(),
                dto.email(),
                dto.senha(),
                tipoDoUsuario
        );

        Usuario usuarioSalvo = usuarioService.registrarUsuario(novoUsuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
    }
}
