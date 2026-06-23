package com.edu.academic_plataform.assignment_service.api.dto;

import java.time.LocalDateTime;

public record TarefaResponseDTO (
        String id,
        String titulo,
        String descricao,
        LocalDateTime dataEntrega,
        String criadoPor
) {}