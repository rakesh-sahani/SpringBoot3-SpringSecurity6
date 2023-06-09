package com.springboot3springsecurity6.exception;

import com.springboot3springsecurity6.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerController {

    private static Date timeStamp = new Date();

    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<ResponseDTO> defaultErrorHandler(Exception exception) {
        ResponseDTO response = new ResponseDTO().builder()
                .response(exception.getMessage())
                .timeStamp(timeStamp)
                .status(HttpStatus.OK.value())
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(value = {CustomException.class})
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<ResponseDTO> CustomHandler(CustomException exception) {
        ResponseDTO response = new ResponseDTO().builder()
                .response(exception.getMessage())
                .timeStamp(timeStamp)
                .status(exception.getStatus())
                .code(exception.getCode()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ResponseDTO response = new ResponseDTO().builder()
                .response(errors)
                .timeStamp(timeStamp)
                .status(HttpStatus.BAD_REQUEST.value())
                .code(HttpStatus.BAD_REQUEST.value()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(value = {IOException.class})
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<ResponseDTO> CustomIOExceptionHandler(IOException exception) {
        ResponseDTO response = new ResponseDTO().builder()
                .response(exception.getMessage())
                .timeStamp(timeStamp)
                .status(HttpStatus.FORBIDDEN.value())
                .code(HttpStatus.FORBIDDEN.value()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}