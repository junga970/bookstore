package com.example.bookstore.config.jwt;

import static com.example.bookstore.type.ErrorCode.USER_NOT_FOUND;

import com.example.bookstore.dto.Token;
import com.example.bookstore.exception.CustomException;
import com.example.bookstore.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

	@Value("${jwt.token.secret-key}")
	private String secretKey;

	private final long tokenValidTime = 30 * 60 * 1000L;
	private final UserDetailsService userDetailsService;
	private final UserRepository userRepository;

	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}

	public Token createToken(String email, List<String> roles) {

		Claims claims = Jwts.claims().setSubject(email);
		claims.put("roles", roles);
		Date now = new Date();

		String accessToken = Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + tokenValidTime))
			.signWith(SignatureAlgorithm.HS256, secretKey)
			.compact();

		return new Token(accessToken);
	}

	public Authentication getAuthentication(String token) {

		UserDetails userDetails = userDetailsService.loadUserByUsername(getUserEmail(token));

		return new UsernamePasswordAuthenticationToken(
			userDetails, "", userDetails.getAuthorities());
	}

	public String getUserEmail(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}

	public String resolveToken(HttpServletRequest request) {
		return request.getHeader("Authorization");
	}

	public boolean validateToken(String jwtToken) {

		Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken).getBody();
		String email = claims.getSubject();

		userRepository.findByEmail(email)
			.orElseThrow(() -> new CustomException(USER_NOT_FOUND, HttpStatus.NOT_FOUND));

		return claims.getExpiration().after(new Date());
	}
}
