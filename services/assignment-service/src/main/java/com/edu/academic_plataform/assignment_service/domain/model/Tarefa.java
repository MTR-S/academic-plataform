package com.edu.academic_plataform.assignment_service.domain.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Tarefa {

    private Long id;
    private String titulo;
    private String descricao;
    private LocalDateTime dataEntrega;

    private String criadoPor;
}

