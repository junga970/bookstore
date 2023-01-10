package com.example.bookstore.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {

	CREATE_USER("회원가입 성공"),
	LOGIN_SUCCESSFUL("로그인 성공"),
	GET_BOOKS("도서 조회 성공"),
	SEARCH_BOOKS("도서 검색 성공");

	private final String message;
}
