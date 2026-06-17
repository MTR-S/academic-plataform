package com.edu.academic_plataform.auth_service.api.security;

import com.edu.academic_plataform.auth_service.domain.model.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service

public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    public String gerarToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer("auth-service-academic") // Quem gerou o token
                    .withSubject(usuario.getEmail()) // O "dono" do token
                    .withClaim("id", usuario.getId()) // Informação extra (Payload) útil para o front-end
                    .withClaim("tipo", usuario.getTipo().name()) // ALUNO ou PROFESSOR
                    .withExpiresAt(gerarDataExpiracao()) // Tempo de vida do token
                    .sign(algorithm); // Assina criptograficamente
        } catch (JWTCreationException exception){
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    private Instant gerarDataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
