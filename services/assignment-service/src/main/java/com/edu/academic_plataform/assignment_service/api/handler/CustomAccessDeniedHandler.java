package com.edu.academic_plataform.assignment_service.api.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler{

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String jsonResponse = "{"
                + "\"status\": 403,"
                + "\"erro\": \"Acesso Negado\","
                + "\"mensagem\": \"Você não tem permissão para executar esta ação. Apenas perfis 'PROFESSOR' podem criar tarefas.\""
                + "}";

        response.getWriter().write(jsonResponse);
    }

}
