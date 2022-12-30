package com.example.bookstore.service;

import com.example.bookstore.dto.BaseResponse;
import com.example.bookstore.dto.UserRegister;
import com.example.bookstore.entity.User;
import com.example.bookstore.repository.UserRepository;
import com.example.bookstore.type.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import static com.example.bookstore.type.ErrorCode.EMAIL_ALREADY_EXISTS;
import static com.example.bookstore.type.ResponseCode.CREATE_USER;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public BaseResponse register(UserRegister request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException(EMAIL_ALREADY_EXISTS.getMessage());
        }

        String encodingPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());

        userRepository.save(User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(encodingPassword)
                .phone(request.getPhone())
                .address(request.getAddress())
                .role(UserRole.USER)
                .build()
        );

        return BaseResponse.response(CREATE_USER, CREATE_USER.getMessage());
    }
}
