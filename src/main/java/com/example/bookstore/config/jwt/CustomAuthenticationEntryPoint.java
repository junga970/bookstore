package com.example.bookstore.config.jwt;

import com.example.bookstore.type.ErrorCode;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component()
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(
		HttpServletRequest request,
		HttpServletResponse response,
		AuthenticationException authException)
		throws IOException, ServletException {

		ErrorCode errorCode = (ErrorCode) request.getAttribute("errorCode");
		HttpStatus httpStatus = (HttpStatus) request.getAttribute("httpStatus");
		setResponse(response, errorCode, httpStatus);
	}

	private void setResponse(
		HttpServletResponse response,
		ErrorCode errorCode,
		HttpStatus httpStatus)
		throws IOException {

		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(httpStatus.value());

		JSONObject responseJson = new JSONObject();
		responseJson.put("errorCode", errorCode.toString());
		responseJson.put("errorMessage", errorCode.getMessage());
		response.getWriter().print(responseJson);
	}
}
