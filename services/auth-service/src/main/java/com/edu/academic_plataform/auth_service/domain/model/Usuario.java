package com.edu.academic_plataform.auth_service.domain.model;

import com.edu.academic_plataform.auth_service.domain.enums.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    private Long id;
    private String nome;
    private String email;
    private String senha;
    private TipoUsuario tipo;
}
