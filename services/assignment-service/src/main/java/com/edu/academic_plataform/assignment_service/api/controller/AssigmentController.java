package com.edu.academic_plataform.assignment_service.api.controller;

import com.edu.academic_plataform.assignment_service.api.dto.MensagemResponseDTO;
import com.edu.academic_plataform.assignment_service.api.dto.TarefaRequestDTO;
import com.edu.academic_plataform.assignment_service.api.dto.TarefaResponseDTO;
import com.edu.academic_plataform.assignment_service.domain.model.Tarefa;
import com.edu.academic_plataform.assignment_service.service.TarefaService;
import com.edu.academic_plataform.assignment_service.database.TarefaEntity;
import com.edu.academic_plataform.assignment_service.persistence.repository.TarefaRepository;
import jakarta.validation.Valid;
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

        TarefaResponseDTO tarefaResponseDTO = tarefaService.salvar(tarefaDto, principal);

        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(tarefaResponseDTO);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<TarefaResponseDTO>> listarTodas() {
        List<TarefaResponseDTO> listaDeTarefas =  tarefaService.listarTodas();

        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(listaDeTarefas);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MensagemResponseDTO> eliminarTarefa(@PathVariable Long id) {
        tarefaService.deletar(id);

        MensagemResponseDTO response = new MensagemResponseDTO(
                200,
                "Tarefa de ID " + id + " apagada com sucesso."
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarefaResponseDTO> atualizarTarefa(
            @PathVariable Long id,
            @RequestBody @Valid TarefaRequestDTO tarefaDto) {

        TarefaResponseDTO tarefaAtualizada = tarefaService.atualizar(id, tarefaDto);

        return ResponseEntity.ok(tarefaAtualizada);
    }
}
