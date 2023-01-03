package com.example.bookstore.dto.response;

import com.example.bookstore.type.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
@Builder
public class ErrorResponse {

	private ErrorCode errorCode;
	private String errorMessage;

	public static ResponseEntity<ErrorResponse> response(ErrorCode errorCode, HttpStatus status) {

		return ResponseEntity
			.status(status)
			.body(ErrorResponse.builder()
				.errorCode(errorCode)
				.errorMessage(errorCode.getMessage())
				.build()
			);
	}
}
