package com.teste.config;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Throwable> {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Override
    public Response toResponse(Throwable exception) {
        logger.error("Erro não tratado na API", exception);

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", 500);
        errorResponse.put("mensagem", "Erro inesperado no servidor");

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse).type("application/json").build();
    }
}
