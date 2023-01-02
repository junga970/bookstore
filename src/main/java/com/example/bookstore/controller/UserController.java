package com.example.bookstore.controller;

import static com.example.bookstore.type.ResponseCode.CREATE_USER;
import static com.example.bookstore.type.ResponseCode.LOGIN_SUCCESSFUL;

import com.example.bookstore.dto.Token;
import com.example.bookstore.dto.UserLogin;
import com.example.bookstore.dto.UserRegister;
import com.example.bookstore.dto.response.ApiResponse;
import com.example.bookstore.service.UserService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

	private final UserService userService;

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public ApiResponse register(@RequestBody @Valid UserRegister request) {

		userService.register(request);

		return new ApiResponse(CREATE_USER);
	}

	@PostMapping("/login")
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse<Token> login(@RequestBody @Valid UserLogin request) {

		Token accessToken = userService.login(request);

		return new ApiResponse(accessToken, LOGIN_SUCCESSFUL);
	}
}
