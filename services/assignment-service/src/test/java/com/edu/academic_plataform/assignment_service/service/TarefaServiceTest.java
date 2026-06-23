package com.edu.academic_plataform.assignment_service.service;

import com.edu.academic_plataform.assignment_service.api.dto.TarefaRequestDTO;
import com.edu.academic_plataform.assignment_service.api.dto.TarefaResponseDTO;
import com.edu.academic_plataform.assignment_service.database.TarefaEntity;
import com.edu.academic_plataform.assignment_service.domain.exception.RecursoNaoEncontradoException;
import com.edu.academic_plataform.assignment_service.domain.model.Tarefa;
import com.edu.academic_plataform.assignment_service.persistence.mapper.TarefaMapper;
import com.edu.academic_plataform.assignment_service.persistence.repository.TarefaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TarefaServiceTest {

    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private TarefaMapper tarefaMapper;

    @Mock
    private Principal principal;

    @InjectMocks
    private TarefaService tarefaService;

    @Test
    @DisplayName("Deve salvar uma tarefa com sucesso definindo o criador a partir do Principal")
    void deveSalvarTarefaComSucesso() {
        // Arrange
        TarefaRequestDTO requestDTO = new TarefaRequestDTO("Título", "Desc", LocalDateTime.now().plusDays(1));
        Tarefa domain = new Tarefa();
        TarefaEntity entity = new TarefaEntity();
        TarefaEntity entitySalva = new TarefaEntity();
        TarefaResponseDTO responseDTO = new TarefaResponseDTO("1", "Título", "Desc", LocalDateTime.now().plusDays(1), "prof@teste.com");

        when(tarefaMapper.toDomain(requestDTO)).thenReturn(domain);
        when(principal.getName()).thenReturn("prof@teste.com");
        when(tarefaMapper.toEntity(domain)).thenReturn(entity);
        when(tarefaRepository.save(entity)).thenReturn(entitySalva);
        when(tarefaMapper.toDto(entitySalva)).thenReturn(responseDTO);

        // Act
        TarefaResponseDTO resultado = tarefaService.salvar(requestDTO, principal);

        // Assert
        assertNotNull(resultado);
        assertEquals("prof@teste.com", resultado.criadoPor());
        assertEquals("Título", resultado.titulo());

        // Verifica se os métodos foram chamados a quantidade correta de vezes
        verify(tarefaRepository, times(1)).save(entity);
        verify(tarefaMapper, times(1)).toDomain(requestDTO);
    }

    @Test
    @DisplayName("Deve listar todas as tarefas convertendo Entidade para DTO")
    void deveListarTodasAsTarefas() {
        // Arrange
        TarefaEntity entity1 = new TarefaEntity();
        TarefaEntity entity2 = new TarefaEntity();
        TarefaResponseDTO dto1 = new TarefaResponseDTO("1", "T1", "D1", null, "p1");
        TarefaResponseDTO dto2 = new TarefaResponseDTO("2", "T2", "D2", null, "p2");

        when(tarefaRepository.findAll()).thenReturn(List.of(entity1, entity2));
        when(tarefaMapper.toDto(entity1)).thenReturn(dto1);
        when(tarefaMapper.toDto(entity2)).thenReturn(dto2);

        // Act
        List<TarefaResponseDTO> resultado = tarefaService.listarTodas();

        // Assert
        assertEquals(2, resultado.size());
        verify(tarefaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve deletar a tarefa quando o ID existir no banco")
    void deveDeletarQuandoIdExistir() {
        // Arrange
        String id = "id-valido";
        when(tarefaRepository.existsById(id)).thenReturn(true);

        // Act
        assertDoesNotThrow(() -> tarefaService.deletar(id));

        // Assert
        verify(tarefaRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Deve lançar RecursoNaoEncontradoException ao tentar deletar ID inexistente")
    void deveLancarExcecaoAoDeletarIdInexistente() {
        // Arrange
        String id = "id-invalido";
        when(tarefaRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class, () -> {
            tarefaService.deletar(id);
        });

        assertEquals("Tarefa não encontrada com o ID: id-invalido", exception.getMessage());
        verify(tarefaRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Deve atualizar a tarefa com sucesso quando o ID existir")
    void deveAtualizarTarefaComSucesso() {
        // Arrange
        String id = "id-valido";
        TarefaRequestDTO requestDTO = new TarefaRequestDTO("Novo Título", "Nova Desc", LocalDateTime.now().plusDays(2));
        TarefaEntity entidadeExistente = new TarefaEntity();
        TarefaEntity entidadeSalva = new TarefaEntity();
        TarefaResponseDTO responseDTO = new TarefaResponseDTO(id, "Novo Título", "Nova Desc", null, "prof");

        when(tarefaRepository.findById(id)).thenReturn(Optional.of(entidadeExistente));

        when(tarefaRepository.save(entidadeExistente)).thenReturn(entidadeSalva);
        when(tarefaMapper.toDto(entidadeSalva)).thenReturn(responseDTO);

        // Act
        TarefaResponseDTO resultado = tarefaService.atualizar(id, requestDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals("Novo Título", resultado.titulo());

        verify(tarefaMapper, times(1)).atualizarEntidadeDoDto(requestDTO, entidadeExistente);
        verify(tarefaRepository, times(1)).save(entidadeExistente);
    }

    @Test
    @DisplayName("Deve lançar RecursoNaoEncontradoException ao tentar atualizar ID inexistente")
    void deveLancarExcecaoAoAtualizarIdInexistente() {
        // Arrange
        String id = "id-invalido";
        TarefaRequestDTO requestDTO = new TarefaRequestDTO("Título", "Desc", null);

        when(tarefaRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class, () -> {
            tarefaService.atualizar(id, requestDTO);
        });

        assertEquals("Tarefa não encontrada com o ID: id-invalido", exception.getMessage());
        verify(tarefaMapper, never()).atualizarEntidadeDoDto(any(), any());
        verify(tarefaRepository, never()).save(any());
    }
}
