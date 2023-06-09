package com.springboot3springsecurity6.service;

import com.springboot3springsecurity6.dto.ChangePassword;
import com.springboot3springsecurity6.dto.ResponseDTO;
import com.springboot3springsecurity6.dto.SignIn;
import com.springboot3springsecurity6.dto.SignUp;
import com.springboot3springsecurity6.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AccessService {

    ResponseDTO signIn(SignIn signIn) throws CustomException;

    ResponseDTO signUp(SignUp signUp) throws CustomException;

    ResponseDTO refreshToken(HttpServletRequest request, HttpServletResponse response) throws CustomException, IOException;

    ResponseDTO updatePassword(HttpServletRequest request, ChangePassword changePassword) throws CustomException, IOException;
}