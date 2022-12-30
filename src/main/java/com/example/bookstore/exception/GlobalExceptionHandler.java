package com.example.bookstore.exception;

import com.example.bookstore.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.bookstore.type.ErrorCode.VALIDATION_FAILED;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> messages = new ArrayList<>();
        e.getBindingResult().getAllErrors().forEach( error -> {
            messages.add(error.getDefaultMessage());
        });

        return new ResponseEntity(
                ErrorResponse.response(VALIDATION_FAILED, VALIDATION_FAILED.getMessage(), messages), HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgumentException(IllegalArgumentException e) {
        Map<String, String> message = new HashMap<>();
        message.put("message", e.getMessage());

        return new ResponseEntity(message, HttpStatus.CONFLICT);
    }
}
