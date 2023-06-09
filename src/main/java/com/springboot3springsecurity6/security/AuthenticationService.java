package com.springboot3springsecurity6.security;

import com.springboot3springsecurity6.dto.SignIn;
import com.springboot3springsecurity6.dto.TokenAuthenticationResponse;
import com.springboot3springsecurity6.entities.MasterUserEntity;
import com.springboot3springsecurity6.entities.TokenEntity;
import com.springboot3springsecurity6.enums.TokenType;
import com.springboot3springsecurity6.repository.MasterUserRepository;
import com.springboot3springsecurity6.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final MasterUserRepository masterUserRepository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public TokenAuthenticationResponse request(MasterUserEntity masterUserEntity) {
        MasterUserEntity savedUser = masterUserRepository.save(masterUserEntity);
        String jwtToken = jwtService.generateToken(masterUserEntity);
        String refreshToken = jwtService.generateRefreshToken(masterUserEntity);
        saveUserToken(savedUser, jwtToken);
        return TokenAuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public TokenAuthenticationResponse authenticate(SignIn request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        MasterUserEntity user = masterUserRepository.findByEmail(request.getEmail()).orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return TokenAuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(MasterUserEntity masterUserEntity, String jwtToken) {
        TokenEntity token = TokenEntity.builder()
                .masterUserId(masterUserEntity)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(MasterUserEntity masterUserEntity) {
        List<TokenEntity> validUserTokens = tokenRepository.findByMasterUserId_IdAndRevokedFalseOrExpiredFalse(UUID.fromString(masterUserEntity.getId().toString()));
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public TokenAuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        TokenAuthenticationResponse authResponse = null;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            MasterUserEntity user = this.masterUserRepository.findByEmail(userEmail).orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                String accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                authResponse = TokenAuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
            }
        }
        return authResponse;
    }
}
