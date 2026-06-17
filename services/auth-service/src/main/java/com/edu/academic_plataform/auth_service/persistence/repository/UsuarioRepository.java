package com.edu.academic_plataform.auth_service.persistence.repository;

import com.edu.academic_plataform.auth_service.database.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    Optional<UsuarioEntity> findByEmail(String email);

    Boolean existsByEmail(String email);
}

