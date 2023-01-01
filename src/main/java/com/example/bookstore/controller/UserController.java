package com.example.bookstore.controller;

import com.example.bookstore.dto.response.BaseResponse;
import com.example.bookstore.dto.response.DataResponse;
import com.example.bookstore.dto.UserLogin;
import com.example.bookstore.dto.UserRegister;
import com.example.bookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid UserRegister request) {
        BaseResponse response = userService.register(request);

        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid UserLogin request) {
        DataResponse response = userService.login(request);

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/test")
    public ResponseEntity test() {

        return new ResponseEntity("response", HttpStatus.OK);
    }
}
