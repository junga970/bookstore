package com.example.bookstore.dto.response;

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

    public static BaseResponse response(ResponseCode responseCode) {
        return BaseResponse.builder()
                .responseCode(responseCode)
                .message(responseCode.getMessage())
                .build();
    }
}
