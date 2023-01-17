package com.example.bookstore.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {

	CREATE_USER("회원가입 성공"),
	LOGIN_SUCCESSFUL("로그인 성공"),
	SEARCH_BOOKS("도서 검색 성공"),
	GET_BOOKS("도서 조회 성공"),
	GET_CART("장바구니 조회 성공"),
	DELETE_CART("장바구니 상품 전체 삭제 성공"),
	ADD_BOOK_TO_CART("장바구니 도서 추가 성공"),
	DELETE_BOOK_IN_CART("장바구니 도서 삭제 성공"),
	UPDATE_QUANTITY_OF_BOOK_IN_CART("장바구니 도서 수량 업데이트 성공"),
	NOW_DREAM_ORDER_SUCCESSFUL("나우드림 주문 성공"),
	GET_STOCK_BY_STORES("매장별 재고 조회 성공");

	private final String message;
}
