package com.example.bookstore.service;


import static com.example.bookstore.type.ErrorCode.EMAIL_ALREADY_EXISTS;
import static com.example.bookstore.type.ErrorCode.INVALID_EMAIL;
import static com.example.bookstore.type.ErrorCode.INVALID_PASSWORD;

import com.example.bookstore.config.jwt.JwtTokenProvider;
import com.example.bookstore.dto.Token;
import com.example.bookstore.dto.LoginRequest;
import com.example.bookstore.dto.RegisterRequest;
import com.example.bookstore.entity.User;
import com.example.bookstore.exception.CustomException;
import com.example.bookstore.repository.UserRepository;
import com.example.bookstore.type.UserRole;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final PasswordEncoder passwordEncoder;

	public void register(RegisterRequest request) {

		if (userRepository.existsByEmail(request.getEmail())) {
			throw new CustomException(EMAIL_ALREADY_EXISTS, HttpStatus.CONFLICT);
		}

		String encodingPassword = passwordEncoder.encode(request.getPassword());

		userRepository.save(User.builder()
			.name(request.getName())
			.email(request.getEmail())
			.password(encodingPassword)
			.phone(request.getPhone())
			.address(request.getAddress())
			.role(UserRole.ROLE_USER)
			.build()
		);
	}

	public Token login(LoginRequest request) {

		User user = userRepository.findByEmail(request.getEmail())
			.orElseThrow(() -> new CustomException(INVALID_EMAIL, HttpStatus.UNAUTHORIZED));

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new CustomException(INVALID_PASSWORD, HttpStatus.UNAUTHORIZED);
		}

		List<String> roles = new ArrayList<>();
		roles.add(user.getRole().toString());

		return jwtTokenProvider.createToken(user.getEmail(), roles);
	}
}
