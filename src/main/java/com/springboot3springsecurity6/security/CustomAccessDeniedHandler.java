package com.springboot3springsecurity6.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot3springsecurity6.dto.ResponseDTO;
import com.springboot3springsecurity6.util.AppConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.util.Date;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private static Date timeStamp = new Date();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exc) throws IOException {
        ResponseDTO responseDTO = new ResponseDTO().builder()
                .timeStamp(timeStamp)
                .status(HttpStatus.FORBIDDEN.value())
                .code(HttpStatus.FORBIDDEN.value())
                .response(AppConstants.INVALID_TOKEN).build();
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), responseDTO);
    }
}
