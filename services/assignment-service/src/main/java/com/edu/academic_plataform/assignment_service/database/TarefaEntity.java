package com.edu.academic_plataform.assignment_service.database;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "tarefas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TarefaEntity {

    @Id
    private String id;

    private String titulo;
    private String descricao;
    private LocalDateTime dataEntrega;

    private String criadoPor;
}
