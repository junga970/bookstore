package com.example.bookstore.dto.response;

import com.example.bookstore.type.ErrorCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
public class ValidityErrorResponse extends ErrorResponse {
    private List<String> messages;

    public static ValidityErrorResponse response (ErrorCode errorCode, List<String> messages) {
        return ValidityErrorResponse.builder()
                .errorCode(errorCode)
                .errorMessage(errorCode.getMessage())
                .messages(messages)
                .build();
    }
}
