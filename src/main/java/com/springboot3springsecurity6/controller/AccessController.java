package com.springboot3springsecurity6.controller;

import com.springboot3springsecurity6.dto.ChangePassword;
import com.springboot3springsecurity6.dto.ResponseDTO;
import com.springboot3springsecurity6.dto.SignIn;
import com.springboot3springsecurity6.dto.SignUp;
import com.springboot3springsecurity6.exception.CustomException;
import com.springboot3springsecurity6.service.AccessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static com.springboot3springsecurity6.util.AppConstants.APP_VERSION_1;


@RestController
@RequestMapping(APP_VERSION_1)
@RequiredArgsConstructor
@Tag(name = "Registration", description = "User  APIs")
public class AccessController {

    private final AccessService accessService;

    @Operation(
            summary = "Sign Up",
            description = "User Signup API")
    @PostMapping("/sign-up")
    public ResponseEntity<ResponseDTO> signUp(@Valid @RequestBody SignUp signUp) throws CustomException {
        return new ResponseEntity<>(accessService.signUp(signUp), HttpStatus.OK);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<ResponseDTO> signIn(@Valid @RequestBody SignIn signIn) throws CustomException {
        return new ResponseEntity<>(accessService.signIn(signIn), HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ResponseDTO> refreshToken(HttpServletRequest request, HttpServletResponse response) throws CustomException, IOException {
        return new ResponseEntity<>(accessService.refreshToken(request, response), HttpStatus.OK);
    }

    @PostMapping("/update-password")
    public ResponseEntity<ResponseDTO> updatePassword(HttpServletRequest request, @Valid @RequestBody ChangePassword changePassword) throws CustomException, IOException {
        return new ResponseEntity<>(accessService.updatePassword(request, changePassword), HttpStatus.OK);
    }

    @PostMapping("/sign-out")
    public void signOut() {
    }
}
