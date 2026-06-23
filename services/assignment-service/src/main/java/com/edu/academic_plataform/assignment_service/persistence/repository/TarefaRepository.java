package com.edu.academic_plataform.assignment_service.persistence.repository;

import com.edu.academic_plataform.assignment_service.database.TarefaEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TarefaRepository extends MongoRepository<TarefaEntity, String>{
}
