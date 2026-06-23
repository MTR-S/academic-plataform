package com.edu.academic_plataform.assignment_service.api.controller;

import com.edu.academic_plataform.assignment_service.api.dto.TarefaRequestDTO;
import com.edu.academic_plataform.assignment_service.api.dto.TarefaResponseDTO;
import com.edu.academic_plataform.assignment_service.service.TarefaService;
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
public class AssigmentController {

    private final TarefaService tarefaService;

    public AssigmentController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @PostMapping
    public ResponseEntity<TarefaResponseDTO> criarTarefa(@RequestBody TarefaRequestDTO tarefaDto, Principal principal) {
        // O 'principal' contém o e-mail injetado pelo nosso SecurityFilter

        TarefaResponseDTO tarefaResponseDTO = tarefaService.salvar(tarefaDto, principal);

        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(tarefaResponseDTO);
    }
/*
    @GetMapping
    public ResponseEntity<List<Tarefa>> listarTodas() {
        List<TarefaResponseDTO> listaDeTarefas =  tarefaService.listarTodas();

        return ResponseEntity.ok(tarefaRepository.findAll());
    }*/
}
