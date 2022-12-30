package com.example.bookstore.dto;

import com.example.bookstore.type.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private ErrorCode errorCode;
    private String errorMessage;
    private List<String> messages;

    public static ErrorResponse response (ErrorCode errorCode, String errorMessage, List<String> messages) {
        return ErrorResponse.builder()
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .messages(messages)
                .build();
    }
}
