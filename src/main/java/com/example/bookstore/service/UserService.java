package com.example.bookstore.service;


import com.example.bookstore.config.jwt.JwtTokenProvider;
import com.example.bookstore.dto.Token;
import com.example.bookstore.dto.UserLogin;
import com.example.bookstore.dto.UserRegister;
import com.example.bookstore.dto.response.BaseResponse;
import com.example.bookstore.dto.response.DataResponse;
import com.example.bookstore.entity.User;
import com.example.bookstore.exception.CustomException;
import com.example.bookstore.repository.UserRepository;
import com.example.bookstore.type.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.bookstore.type.ErrorCode.*;
import static com.example.bookstore.type.ResponseCode.CREATE_USER;
import static com.example.bookstore.type.ResponseCode.LOGIN_SUCCESSFUL;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public BaseResponse register(UserRegister request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(EMAIL_ALREADY_EXISTS, HttpStatus.CONFLICT);
        }

        String encodingPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());

        userRepository.save(User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(encodingPassword)
                .phone(request.getPhone())
                .address(request.getAddress())
                .role(UserRole.ROLE_USER)
                .build()
        );

        return BaseResponse.response(CREATE_USER);
    }

    public DataResponse login(UserLogin request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(INVALID_EMAIL, HttpStatus.UNAUTHORIZED));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new CustomException(INVALID_PASSWORD, HttpStatus.UNAUTHORIZED);
        }

        List<String> roles = new ArrayList<>();
        roles.add(user.getRole().toString());

        Token accessToken = jwtTokenProvider.createToken(user.getEmail(), roles);

        return DataResponse.response(LOGIN_SUCCESSFUL, accessToken);
    }
}
