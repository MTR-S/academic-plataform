package com.edu.academic_plataform.auth_service.persistence.mapper;

import com.edu.academic_plataform.auth_service.database.UsuarioEntity;
import com.edu.academic_plataform.auth_service.domain.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    Usuario toDomain(UsuarioEntity entity);

    @Mapping(target = "id", ignore = true)
    UsuarioEntity toEntity(Usuario domain);
}
