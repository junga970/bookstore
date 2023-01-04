package com.example.bookstore.exception;

import static com.example.bookstore.type.ErrorCode.METHOD_ARGUMENT_NOT_VALID;
import static com.example.bookstore.type.ErrorCode.METHOD_ARGUMENT_TYPE_MISMATCH;

import com.example.bookstore.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {

		return ErrorResponse.response(e.getErrorCode(), e.getHttpStatus());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
		MethodArgumentNotValidException e) {

		return ErrorResponse.response(METHOD_ARGUMENT_NOT_VALID, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
		MethodArgumentTypeMismatchException e) {

		return ErrorResponse.response(METHOD_ARGUMENT_TYPE_MISMATCH, HttpStatus.BAD_REQUEST);
	}
}
