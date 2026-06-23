package com.edu.academic_plataform.assignment_service.api.controller;

import com.edu.academic_plataform.assignment_service.api.dto.MensagemResponseDTO;
import com.edu.academic_plataform.assignment_service.domain.exception.RecursoNaoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<MensagemResponseDTO> handleRecursoNaoEncontrado(RecursoNaoEncontradoException ex) {
        MensagemResponseDTO response = new MensagemResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
