package com.example.bookstore.dto;

import com.example.bookstore.type.ResponseCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class BaseResponse {
    private ResponseCode responseCode;
    private String message;

    public static BaseResponse response(final ResponseCode responseCode, final String message) {
        return BaseResponse.builder()
                .responseCode(responseCode)
                .message(message)
                .build();
    }
}
