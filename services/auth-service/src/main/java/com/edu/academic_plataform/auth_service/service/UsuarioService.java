package com.edu.academic_plataform.auth_service.service;

import com.edu.academic_plataform.auth_service.api.dto.UsuarioLoginDTO;
import com.edu.academic_plataform.auth_service.api.security.JwtService;
import com.edu.academic_plataform.auth_service.database.UsuarioEntity;
import com.edu.academic_plataform.auth_service.domain.model.Usuario;
import com.edu.academic_plataform.auth_service.persistence.mapper.UsuarioMapper;
import com.edu.academic_plataform.auth_service.persistence.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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
        log.info("Iniciando tentativa de autenticação para o e-mail: {}", loginDTO.email());

        UsuarioEntity usuarioEntity = usuarioRepository.findByEmail(loginDTO.email())
                .orElseThrow(() -> {
                    log.warn("Falha na autenticação: E-mail não encontrado na base de dados - {}", loginDTO.email());
                    return new IllegalArgumentException("Usuário ou senha inválidos.");
                });

        comparaSenha(loginDTO, usuarioEntity);

        Usuario usuarioValidado = usuarioMapper.toDomain(usuarioEntity);
        String token = jwtService.gerarToken(usuarioValidado);

        log.info("Autenticação realizada com sucesso. Token gerado para o e-mail: {}", loginDTO.email());
        return token;
    }

    @Transactional
    public Usuario registrarUsuario(Usuario novoUsuario) {
        log.info("Iniciando processo de registro para o novo usuário: {}", novoUsuario.getEmail());

        if (usuarioRepository.existsByEmail(novoUsuario.getEmail())) {
            log.warn("Falha no registro: Tentativa de cadastro com e-mail já existente - {}", novoUsuario.getEmail());
            throw new IllegalArgumentException("Email já cadastrado.");
        }

        String senhaCriptografada = passwordEncoder.encode(novoUsuario.getSenha());
        novoUsuario.setSenha(senhaCriptografada);

        UsuarioEntity entity = usuarioMapper.toEntity(novoUsuario);

        UsuarioEntity entitySalva = usuarioRepository.save(entity);

        log.info("Usuário registrado com sucesso no sistema. ID gerado: {}", entitySalva.getId());
        return usuarioMapper.toDomain(entitySalva);
    }

    private void comparaSenha(UsuarioLoginDTO loginDTO, UsuarioEntity usuarioEntity) {
        if (!passwordEncoder.matches(loginDTO.senha(), usuarioEntity.getSenha())) {
            log.warn("Falha na autenticação: Senha incorreta fornecida para o e-mail - {}", loginDTO.email());
            throw new IllegalArgumentException("Usuário ou senha inválidos.");
        }
    }
}