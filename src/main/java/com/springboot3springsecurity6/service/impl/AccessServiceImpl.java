package com.springboot3springsecurity6.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.springboot3springsecurity6.dto.*;
import com.springboot3springsecurity6.entities.MasterUserEntity;
import com.springboot3springsecurity6.enums.Role;
import com.springboot3springsecurity6.exception.CustomException;
import com.springboot3springsecurity6.repository.MasterUserRepository;
import com.springboot3springsecurity6.security.AuthenticationService;
import com.springboot3springsecurity6.security.JwtService;
import com.springboot3springsecurity6.service.AccessService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

import static com.springboot3springsecurity6.util.AppConstants.*;

@Service
@RequiredArgsConstructor
public class AccessServiceImpl implements AccessService {

    private final MasterUserRepository masterUserRepository;

    private final AuthenticationService authenticationService;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private static final Date timeStamp = new Date();

    @Override
    public ResponseDTO signIn(SignIn signIn) throws CustomException {
        TokenAuthenticationResponse token = authenticationService.authenticate(signIn);
        if (!token.getAccessToken().isEmpty()) {
            return new ResponseDTO(timeStamp, HttpStatus.OK.value(), HttpStatus.OK.value(), token);
        }
        throw new CustomException(HttpStatus.OK.value(), HttpStatus.CONFLICT.value(), INVALID_PASSWORD_OR_NO_USER);
    }

    @Override
    public ResponseDTO signUp(SignUp signUp) throws CustomException {
        if (masterUserRepository.findByEmail(signUp.getEmail()).isPresent()) {
            throw new CustomException(HttpStatus.OK.value(), HttpStatus.CONFLICT.value(), USER_ALREADY_EXISTS);
        }
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        MasterUserEntity masterUserEntity = MasterUserEntity.builder()
                .firstName(signUp.getFirstName())
                .middleName(signUp.getMiddleName())
                .lastName(signUp.getLastName())
                .email(signUp.getEmail())
                .role(signUp.getRole())
                .createdAt(timeStamp)
                .password(passwordEncoder.encode(signUp.getPassword()))
                .role(Role.valueOf("USER"))
                .build();

        TokenAuthenticationResponse token = authenticationService.request(masterUserEntity);
        if (!token.getAccessToken().isEmpty()) {
            objectNode.put("status", USER_CREATED);
            objectNode.put("access-token", token.getAccessToken());
            objectNode.put("refresh-token", token.getRefreshToken());
        }
        return new ResponseDTO(timeStamp, HttpStatus.OK.value(), HttpStatus.OK.value(), objectNode);
    }

    @Override
    public ResponseDTO refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        TokenAuthenticationResponse token = authenticationService.refreshToken(request, response);
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        if (!token.getRefreshToken().isEmpty()) {
            objectNode.put("status", REFRESH_TOKEN);
            objectNode.put("access-token", token.getAccessToken());
            objectNode.put("refresh-token", token.getRefreshToken());
        }
        return new ResponseDTO(timeStamp, HttpStatus.OK.value(), HttpStatus.OK.value(), objectNode);
    }

    @Override
    public ResponseDTO updatePassword(HttpServletRequest request, ChangePassword changePassword) throws CustomException, IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String token;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new CustomException(HttpStatus.OK.value(), HttpStatus.CONFLICT.value(), INVALID_TOKEN);
        }
        token = authHeader.substring(7);
        userEmail = jwtService.extractUsername(token);
        if (userEmail != null && userEmail.equals(changePassword.getEmail())) {
            MasterUserEntity user = masterUserRepository.findByEmail(userEmail).orElseThrow();
            if (jwtService.isTokenValid(token, user)) {
                user.setPassword(passwordEncoder.encode(changePassword.getPassword()));
                MasterUserEntity savedUser = masterUserRepository.save(user);
                if (!savedUser.getEmail().isEmpty()) {
                    return new ResponseDTO(timeStamp, HttpStatus.OK.value(), HttpStatus.OK.value(), PASSWORD_CHANGE);
                }
            }
        }
        throw new CustomException(HttpStatus.OK.value(), HttpStatus.CONFLICT.value(), INVALID_TOKEN);
    }
}