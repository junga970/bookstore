package com.example.bookstore.dto;

import com.example.bookstore.type.ResponseCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class DataResponse extends BaseResponse{
    private Object data;

    public static DataResponse response(final ResponseCode responseCode, final String message, final Object data) {
        return DataResponse.builder()
                .responseCode(responseCode)
                .message(message)
                .data(data)
                .build();
    }
}
