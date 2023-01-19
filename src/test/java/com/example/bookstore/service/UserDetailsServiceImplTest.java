package com.example.bookstore.service;

import static com.example.bookstore.type.ErrorCode.INVALID_EMAIL;
import static com.example.bookstore.type.UserRole.ROLE_USER;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.example.bookstore.entity.User;
import com.example.bookstore.exception.CustomException;
import com.example.bookstore.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserDetailsServiceImpl userDetailsServiceImpl;

	@Test
	void loadUserByUsernameSuccess() {
		// given
		User user = User.builder()
			.email("test@naver.com")
			.password("testPassword")
			.role(ROLE_USER)
			.build();
		user.setId(1L);

		given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));

		// when
		UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(user.getEmail());

		// then
		assertEquals("1", userDetails.getUsername());
	}

	@Test
	void loadUserByUsernameFailure_InvalidEmail() {
		// given
		String email = "test@naver.com";

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> userDetailsServiceImpl.loadUserByUsername(email));

		// then
		assertEquals(INVALID_EMAIL, exception.getErrorCode());
		assertEquals(HttpStatus.UNAUTHORIZED, exception.getHttpStatus());
	}
}