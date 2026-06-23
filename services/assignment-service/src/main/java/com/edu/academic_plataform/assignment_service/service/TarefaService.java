package com.edu.academic_plataform.assignment_service.service;

import com.edu.academic_plataform.assignment_service.api.dto.TarefaRequestDTO;
import com.edu.academic_plataform.assignment_service.api.dto.TarefaResponseDTO;
import com.edu.academic_plataform.assignment_service.database.TarefaEntity;
import com.edu.academic_plataform.assignment_service.domain.exception.RecursoNaoEncontradoException;
import com.edu.academic_plataform.assignment_service.domain.model.Tarefa;
import com.edu.academic_plataform.assignment_service.persistence.mapper.TarefaMapper;
import com.edu.academic_plataform.assignment_service.persistence.repository.TarefaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Slf4j
@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final TarefaMapper tarefaMapper;

    public TarefaService(TarefaRepository tarefaRepository, TarefaMapper tarefaMapper) {
        this.tarefaRepository = tarefaRepository;
        this.tarefaMapper = tarefaMapper;
    }

    public TarefaResponseDTO salvar(TarefaRequestDTO tarefaDto, Principal principal) {
        log.info("Iniciando criação de nova tarefa. Título: {}, Usuário: {}", tarefaDto.titulo(), principal.getName());

        Tarefa tarefa = tarefaMapper.toDomain(tarefaDto);
        tarefa.setCriadoPor(principal.getName());
        TarefaEntity tarefaEntity = tarefaMapper.toEntity(tarefa);
        TarefaEntity tarefaEntitySalva = tarefaRepository.save(tarefaEntity);

        log.info("Tarefa criada com sucesso. ID gerado: {}", tarefaEntitySalva.getId());
        return tarefaMapper.toDto(tarefaEntitySalva);
    }

    public List<TarefaResponseDTO> listarTodas() {
        log.debug("Buscando todas as tarefas no banco de dados.");
        List<TarefaEntity> entidades = tarefaRepository.findAll();
        log.debug("Total de tarefas encontradas: {}", entidades.size());

        return entidades.stream().map(tarefaMapper::toDto).toList();
    }

    public void deletar(Long id) {
        log.info("Iniciando deleção da tarefa com ID: {}", id);
        if (!tarefaRepository.existsById(id)) {
            log.warn("Tentativa de deleção falhou. Tarefa não encontrada. ID: {}", id);
            throw new RecursoNaoEncontradoException("Tarefa não encontrada com o ID: " + id);
        }
        tarefaRepository.deleteById(id);
        log.info("Tarefa deletada com sucesso. ID: {}", id);
    }

    public TarefaResponseDTO atualizar(Long id, TarefaRequestDTO dto) {
        log.info("Iniciando atualização da tarefa com ID: {}", id);
        TarefaEntity entidadeExistente = tarefaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Atualização falhou. Tarefa não encontrada. ID: {}", id);
                    return new RecursoNaoEncontradoException("Tarefa não encontrada com o ID: " + id);
                });

        tarefaMapper.atualizarEntidadeDoDto(dto, entidadeExistente);
        TarefaEntity entidadeSalva = tarefaRepository.save(entidadeExistente);

        log.info("Tarefa atualizada com sucesso. ID: {}", id);
        return tarefaMapper.toDto(entidadeSalva);
    }
}