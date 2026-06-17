package com.edu.academic_plataform.auth_service.api.dto;

import com.edu.academic_plataform.auth_service.domain.enums.TipoUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UsuarioCadastroDTO(
        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
        String nome,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        String email,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$",
                message = "A senha deve conter ao menos uma letra maiúscula e um caractere especial (@#$%^&+=!)"
        )
        String senha,

        @NotBlank(message = "O tipo de usuário é obrigatório")
        @Pattern(
                regexp = "^(ALUNO|PROFESSOR)$",
                message = "O tipo deve ser exclusivamente ALUNO ou PROFESSOR"
        )
        String tipo
) {}

