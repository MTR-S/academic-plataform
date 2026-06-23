package com.edu.academic_plataform.assignment_service.persistence.mapper;

import com.edu.academic_plataform.assignment_service.api.dto.TarefaRequestDTO;
import com.edu.academic_plataform.assignment_service.api.dto.TarefaResponseDTO;
import com.edu.academic_plataform.assignment_service.database.TarefaEntity;
import com.edu.academic_plataform.assignment_service.domain.model.Tarefa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TarefaMapper {

    @Mapping(target = "criadoPor", ignore = true)
    Tarefa toDomain(TarefaRequestDTO requestDTO);

    TarefaEntity toEntity(Tarefa domain);

    TarefaResponseDTO toDto(TarefaEntity entity);

    Tarefa toDomain(TarefaEntity entity);
}
