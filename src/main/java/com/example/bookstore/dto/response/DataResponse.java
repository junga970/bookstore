package com.example.bookstore.dto.response;

import com.example.bookstore.type.ResponseCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class DataResponse extends BaseResponse{
    private Object data;

    public static DataResponse response(ResponseCode responseCode, Object data) {
        return DataResponse.builder()
                .responseCode(responseCode)
                .message(responseCode.getMessage())
                .data(data)
                .build();
    }
}
