package com.edu.academic_plataform.auth_service.api.controller;

import com.edu.academic_plataform.auth_service.domain.exception.EmailJaCadastradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice

public class GlobalExceptionHandler {

    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<Map<String, Object>> handleEmailDuplicado(EmailJaCadastradoException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.CONFLICT.value());
        body.put("erro", "Conflito de Dados");
        body.put("mensagem", ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidacoesDeCampos(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value()); // HTTP 400: Requisição Mal Formada
        body.put("erro", "Erro de Validação nos Campos");

        Map<String, String> errosDeCampo = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errosDeCampo.put(error.getField(), error.getDefaultMessage())
        );

        body.put("camposInvalidos", errosDeCampo);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}
