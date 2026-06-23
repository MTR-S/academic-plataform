package com.edu.academic_plataform.assignment_service.api.controller;

import com.edu.academic_plataform.assignment_service.database.TarefaEntity;
import com.edu.academic_plataform.assignment_service.persistence.repository.TarefaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
public class AssigmentController {

    private final TarefaRepository tarefaRepository;

    @PostMapping
    public ResponseEntity<Tarefa> criarTarefa(@RequestBody Tarefa tarefa, Principal principal) {
        // O 'principal' contém o e-mail injetado pelo nosso SecurityFilter
        tarefa.setCriadoPor(principal.getName());
        Tarefa salva = tarefaRepository.save(tarefa);
        return ResponseEntity.ok(salva);
    }

    @GetMapping
    public ResponseEntity<List<Tarefa>> listarTodas() {
        return ResponseEntity.ok(tarefaRepository.findAll());
    }
}
