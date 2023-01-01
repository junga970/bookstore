package com.example.bookstore.exception;

import com.example.bookstore.dto.response.ErrorResponse;
import com.example.bookstore.dto.response.ValidityErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

import static com.example.bookstore.type.ErrorCode.VALIDATION_FAILED;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> messages = new ArrayList<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            messages.add(error.getDefaultMessage());
        });

        return new ResponseEntity(ValidityErrorResponse.response(VALIDATION_FAILED, messages), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity handleCustomException(CustomException e) {
        return new ResponseEntity(ErrorResponse.response(e.getErrorCode()),e.getHttpStatus());
    }
}
