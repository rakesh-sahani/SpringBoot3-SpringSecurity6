package com.springboot3springsecurity6.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot3springsecurity6.dto.ResponseDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.util.Date;

import static com.springboot3springsecurity6.util.AppConstants.NO_TOKEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static Date timeStamp = new Date();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ResponseDTO responseDTO = new ResponseDTO().builder()
                .timeStamp(timeStamp)
                .status(HttpStatus.FORBIDDEN.value())
                .code(HttpStatus.FORBIDDEN.value())
                .response(NO_TOKEN).build();
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), responseDTO);
    }
}
