package com.example.bookstore.config.jwt;

import static com.example.bookstore.type.ErrorCode.EXPIRED_TOKEN;
import static com.example.bookstore.type.ErrorCode.INVALID_TOKEN;
import static com.example.bookstore.type.ErrorCode.USER_NOT_FOUND;

import com.example.bookstore.exception.CustomException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {

		try {
			String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);

			if (token != null && jwtTokenProvider.validateToken(token)) {
				Authentication authentication = jwtTokenProvider.getAuthentication(token);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (SignatureException | MalformedJwtException e) {
			request.setAttribute("errorCode", INVALID_TOKEN);
			request.setAttribute("httpStatus", HttpStatus.UNAUTHORIZED);
		} catch (ExpiredJwtException e) {
			request.setAttribute("errorCode", EXPIRED_TOKEN);
			request.setAttribute("httpStatus", HttpStatus.UNAUTHORIZED);
		} catch (CustomException e) {
			request.setAttribute("errorCode", USER_NOT_FOUND);
			request.setAttribute("httpStatus", e.getHttpStatus());
		}

		chain.doFilter(request, response);
	}
}
