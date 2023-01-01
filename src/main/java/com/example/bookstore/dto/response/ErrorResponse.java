package com.example.bookstore.dto.response;

import com.example.bookstore.type.ErrorCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class ErrorResponse {
    private ErrorCode errorCode;
    private String errorMessage;

    public static ErrorResponse response (ErrorCode errorCode) {
        return ErrorResponse.builder()
                .errorCode(errorCode)
                .errorMessage(errorCode.getMessage())
                .build();
    }
}
