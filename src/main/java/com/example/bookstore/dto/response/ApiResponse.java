package com.example.bookstore.dto.response;

import com.example.bookstore.type.ResponseCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
public class ApiResponse<T> {

	private ResponseCode responseCode;
	private String message;

	@Nullable
	private T data;

	public ApiResponse(ResponseCode responseCode) {
		this(null, responseCode);
	}

	public ApiResponse(@Nullable T data, ResponseCode responseCode) {
		this.data = data;
		this.responseCode = responseCode;
		this.message = responseCode.getMessage();
	}
}
