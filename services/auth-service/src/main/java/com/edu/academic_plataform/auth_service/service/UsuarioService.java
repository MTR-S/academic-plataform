package com.edu.academic_plataform.auth_service.service;

import com.edu.academic_plataform.auth_service.api.dto.UsuarioLoginDTO;
import com.edu.academic_plataform.auth_service.api.security.JwtService;
import com.edu.academic_plataform.auth_service.database.UsuarioEntity;
import com.edu.academic_plataform.auth_service.domain.model.Usuario;
import com.edu.academic_plataform.auth_service.persistence.mapper.UsuarioMapper;
import com.edu.academic_plataform.auth_service.persistence.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          UsuarioMapper usuarioMapper,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String autenticar(UsuarioLoginDTO loginDTO) {
        UsuarioEntity usuarioEntity = usuarioRepository.findByEmail(loginDTO.email())
                .orElseThrow(() -> new IllegalArgumentException("Usuário ou senha inválidos."));

        comparaSenha(loginDTO, usuarioEntity);

        Usuario usuarioValidado = usuarioMapper.toDomain(usuarioEntity);
        return jwtService.gerarToken(usuarioValidado);
    }

    @Transactional
    public Usuario registrarUsuario(Usuario novoUsuario) {
        if (usuarioRepository.existsByEmail(novoUsuario.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado.");
        }

        String senhaCriptografada = passwordEncoder.encode(novoUsuario.getSenha());
        novoUsuario.setSenha(senhaCriptografada);

        UsuarioEntity entity = usuarioMapper.toEntity(novoUsuario);

        UsuarioEntity entitySalva = usuarioRepository.save(entity);


        return usuarioMapper.toDomain(entitySalva);
    }

    private void comparaSenha(UsuarioLoginDTO loginDTO, UsuarioEntity usuarioEntity) {

        if (!passwordEncoder.matches(loginDTO.senha(), usuarioEntity.getSenha())) {
            throw new IllegalArgumentException("Usuário ou senha inválidos.");
        }
    }
}
