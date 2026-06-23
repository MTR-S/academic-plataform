package com.edu.academic_plataform.assignment_service.api.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record TarefaRequestDTO(

        @NotBlank(message = "O título é obrigatório")
        String titulo,

        @NotBlank(message = "A descrição é obrigatória")
        String descricao,

        @NotNull(message = "A data de entrega é obrigatória")
        @Future(message = "A data de entrega deve estar no futuro")
        LocalDateTime dataEntrega
) {
}
