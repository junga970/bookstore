package com.example.bookstore.service;


import static com.example.bookstore.type.ErrorCode.EMAIL_ALREADY_EXISTS;
import static com.example.bookstore.type.ErrorCode.INVALID_EMAIL;
import static com.example.bookstore.type.ErrorCode.INVALID_PASSWORD;
import static com.example.bookstore.type.UserRole.ROLE_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.bookstore.config.jwt.JwtTokenProvider;
import com.example.bookstore.dto.Token;
import com.example.bookstore.dto.UserLogin;
import com.example.bookstore.dto.UserRegister;
import com.example.bookstore.entity.User;
import com.example.bookstore.exception.CustomException;
import com.example.bookstore.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private JwtTokenProvider jwtTokenProvider;

	@Spy
	private BCryptPasswordEncoder passwordEncoder;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserService userService;

	/*
		회원가입 테스트
	*/
	@Test
	@DisplayName("회원가입 성공")
	void registerSuccess() {
		// given
		UserRegister request = UserRegister.builder()
			.name("이름원")
			.email("test1@gmail.com")
			.password("test123!")
			.phone("01012345678")
			.build();

		given(userRepository.existsByEmail(anyString())).willReturn(false);

		// when
		userService.register(request);

		// then
		verify(userRepository, times(1)).save(any());
	}

	@Test
	@DisplayName("회원가입 실패 - 이메일 중복")
	void registerFailure_EmailAlreadyExists() {
		// given
		UserRegister request = UserRegister.builder()
			.name("이름원")
			.email("test1@gmail.com")
			.password("test123!")
			.phone("01012345678")
			.build();

		given(userRepository.existsByEmail(anyString())).willReturn(true);

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> userService.register(request));

		// then
		assertEquals(EMAIL_ALREADY_EXISTS, exception.getErrorCode());
		assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus());
	}

	/*
		로그인 테스트
	*/
	@Test
	@DisplayName("로그인 성공")
	void loginSuccess() {
		// given
		String encodingPassword = passwordEncoder.encode("test123!");

		User user = User.builder()
			.name("이름원")
			.email("test1@gmail.com")
			.password(encodingPassword)
			.phone("01012345678")
			.role(ROLE_USER)
			.build();

		UserLogin request = UserLogin.builder()
			.email("test1@gmail.com")
			.password("test123!")
			.build();

		Token token = new Token("token");

		given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));
		given(jwtTokenProvider.createToken(anyString(), anyList())).willReturn(token);

		// when
		Token accessToken = userService.login(request);

		// then
		assertEquals(token, accessToken);
	}

	@Test
	@DisplayName("로그인 실패 - 이메일에 해당하는 사용자 없음")
	void loginFailure_UserNotFound() {
		// given
		UserLogin request = UserLogin.builder()
			.email("test1@gmail.com")
			.password("test123!")
			.build();

		given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> userService.login(request));

		// then
		assertEquals(INVALID_EMAIL, exception.getErrorCode());
		assertEquals(HttpStatus.UNAUTHORIZED, exception.getHttpStatus());
	}

	@Test
	@DisplayName("로그인 실패 - 잘못된 비밀번호")
	void loginFailure_InvalidPassword() {
		// given
		String encodingPassword = passwordEncoder.encode("test123!");
		User user = User.builder()
			.name("이름원")
			.email("test1@gmail.com")
			.password(encodingPassword)
			.phone("01012345678")
			.role(ROLE_USER)
			.build();

		UserLogin request = UserLogin.builder()
			.email("test1@gmail.com")
			.password("test567!")
			.build();

		given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> userService.login(request));

		// then
		assertEquals(INVALID_PASSWORD, exception.getErrorCode());
		assertEquals(HttpStatus.UNAUTHORIZED, exception.getHttpStatus());
	}
}