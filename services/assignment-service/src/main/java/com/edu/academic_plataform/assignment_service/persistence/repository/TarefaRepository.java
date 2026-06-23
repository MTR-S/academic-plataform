package com.edu.academic_plataform.assignment_service.persistence.repository;

import com.edu.academic_plataform.assignment_service.database.TarefaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TarefaRepository extends JpaRepository<TarefaEntity, Long>{
}
