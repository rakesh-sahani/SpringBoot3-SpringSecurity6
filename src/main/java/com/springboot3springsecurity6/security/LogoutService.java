package com.springboot3springsecurity6.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot3springsecurity6.dto.ResponseDTO;
import com.springboot3springsecurity6.entities.TokenEntity;
import com.springboot3springsecurity6.repository.TokenRepository;
import com.springboot3springsecurity6.util.AppConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private static Date timeStamp = new Date();

    private final TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
            final String authHeader = request.getHeader("Authorization");
            final String jwt;
            ResponseDTO responseDTO = new ResponseDTO();
            response.setContentType(APPLICATION_JSON_VALUE);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                responseDTO.builder()
                        .timeStamp(timeStamp)
                        .status(HttpStatus.FORBIDDEN.value())
                        .code(HttpStatus.FORBIDDEN.value())
                        .response(AppConstants.INVALID_TOKEN).build();
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), responseDTO);
                return;
            }
            jwt = authHeader.substring(7);
            TokenEntity storedToken = tokenRepository.findByToken(jwt).orElse(null);
            if (storedToken != null) {
                storedToken.setExpired(true);
                storedToken.setRevoked(true);
                tokenRepository.save(storedToken);
                SecurityContextHolder.clearContext();
            }
            Map<String, Boolean> status = new HashMap<>();
            status.put("status", true);
            new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(response.getOutputStream(), status);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}