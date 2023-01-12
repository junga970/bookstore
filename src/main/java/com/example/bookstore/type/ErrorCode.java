package com.example.bookstore.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	METHOD_ARGUMENT_NOT_VALID("유효성 검사 실패"),
	METHOD_ARGUMENT_TYPE_MISMATCH("파라미터 타입 불일치"),
	HTTP_MASSAGE_NOT_READABLE_EXCEPTION("잘못된 JSON 형식입니다."),
	EMAIL_ALREADY_EXISTS("이미 가입된 이메일입니다."),
	USER_NOT_FOUND("유저를 찾을 수 없습니다."),
	EXPIRED_TOKEN("만료된 토큰입니다."),
	INVALID_TOKEN("유효하지 않은 토큰입니다."),
	INVALID_PASSWORD("잘못된 비밀번호입니다."),
	INVALID_EMAIL("잘못된 이메일입니다."),
	INVALID_ORDER_VALUE("잘못된 정렬 값입니다."),
	INVALID_ORDER_PROPERTY("Order 의 order 속성 값이 잘못됐습니다. (서버 측 오류)"),
	DOES_NOT_EXIST_SUB_CATEGORY_ID("존재하지 않는 서브카테고리입니다."),
	DOES_NOT_EXIST_BOOK_ID("존재하지 않는 도서입니다."),
	DOES_NOT_EXIST_CART_ITEM_ID("존재하지 않는 장바구니 상품입니다.");

	private final String message;
}
