package com.edu.academic_plataform.auth_service.service;

import com.edu.academic_plataform.auth_service.database.UsuarioEntity;
import com.edu.academic_plataform.auth_service.persistence.repository.UsuarioRepository;
import com.edu.academic_plataform.auth_service.domain.enums.TipoUsuario;
import com.edu.academic_plataform.auth_service.domain.model.Usuario;
import com.edu.academic_plataform.auth_service.api.dto.UsuarioLoginDTO;
import com.edu.academic_plataform.auth_service.persistence.mapper.UsuarioMapper;
import com.edu.academic_plataform.auth_service.api.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioDominio;
    private UsuarioEntity usuarioEntity;

    @BeforeEach
    void setUp() {
        usuarioDominio = new Usuario(1L, "Matheus Almeida", "matheus@exemplo.com", "senha123", TipoUsuario.ALUNO);

        usuarioEntity = new UsuarioEntity();
        usuarioEntity.setId(1L);
        usuarioEntity.setNome("Matheus Almeida");
        usuarioEntity.setEmail("matheus@exemplo.com");
        usuarioEntity.setSenha("senha_criptografada_mock");
        usuarioEntity.setTipo(TipoUsuario.ALUNO);
    }

    // --- TESTES DE REGISTRO ---

    @Test
    @DisplayName("Deve registrar um usuário com sucesso quando o e-mail não existir")
    void deveRegistrarUsuarioComSucesso() {
        // GIVEN (Dado que)
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("senha_criptografada_mock");
        when(usuarioMapper.toEntity(any(Usuario.class))).thenReturn(usuarioEntity);
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuarioEntity);
        when(usuarioMapper.toDomain(any(UsuarioEntity.class))).thenReturn(usuarioDominio);

        // WHEN (Quando eu chamar o método)
        Usuario resultado = usuarioService.registrarUsuario(usuarioDominio);

        // THEN (Então eu espero que)
        assertNotNull(resultado);
        assertEquals("Matheus Almeida", resultado.getNome());

        // Verifica se os métodos das dependências foram realmente chamados
        verify(usuarioRepository, times(1)).existsByEmail(anyString());
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(usuarioRepository, times(1)).save(any(UsuarioEntity.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar registrar usuário com e-mail já existente")
    void deveLancarExcecaoQuandoEmailJaExistir() {
        // GIVEN
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(true);

        // WHEN & THEN
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.registrarUsuario(usuarioDominio);
        });

        assertEquals("Email já cadastrado.", exception.getMessage());

        // Garante que se o e-mail existir, o sistema NUNCA tentará salvar no banco
        verify(usuarioRepository, never()).save(any());
    }

    // --- TESTES DE AUTENTICAÇÃO ---

    @Test
    @DisplayName("Deve autenticar e retornar o Token JWT com sucesso")
    void deveAutenticarComSucesso() {
        // GIVEN
        UsuarioLoginDTO loginDTO = new UsuarioLoginDTO("matheus@exemplo.com", "senha123");

        when(usuarioRepository.findByEmail(loginDTO.email())).thenReturn(Optional.of(usuarioEntity));
        when(passwordEncoder.matches(loginDTO.senha(), usuarioEntity.getSenha())).thenReturn(true);
        when(usuarioMapper.toDomain(usuarioEntity)).thenReturn(usuarioDominio);
        when(jwtService.gerarToken(usuarioDominio)).thenReturn("token_jwt_falso_123");

        // WHEN
        String token = usuarioService.autenticar(loginDTO);

        // THEN
        assertNotNull(token);
        assertEquals("token_jwt_falso_123", token);
    }

    @Test
    @DisplayName("Deve lançar exceção ao autenticar com e-mail inexistente")
    void deveLancarExcecaoQuandoEmailNaoExistirNoLogin() {
        // GIVEN
        UsuarioLoginDTO loginDTO = new UsuarioLoginDTO("email_errado@exemplo.com", "senha123");

        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // WHEN & THEN
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.autenticar(loginDTO);
        });

        assertEquals("Usuário ou senha inválidos.", exception.getMessage());
        verify(jwtService, never()).gerarToken(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao autenticar com senha incorreta")
    void deveLancarExcecaoQuandoSenhaEstiverIncorreta() {
        // GIVEN
        UsuarioLoginDTO loginDTO = new UsuarioLoginDTO("matheus@exemplo.com", "senha_errada");

        when(usuarioRepository.findByEmail(loginDTO.email())).thenReturn(Optional.of(usuarioEntity));
        // Simulando que a senha do DTO não bate com a criptografada no banco
        when(passwordEncoder.matches(loginDTO.senha(), usuarioEntity.getSenha())).thenReturn(false);

        // WHEN & THEN
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.autenticar(loginDTO);
        });

        assertEquals("Usuário ou senha inválidos.", exception.getMessage());
        verify(jwtService, never()).gerarToken(any());
    }
}
