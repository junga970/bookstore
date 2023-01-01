package com.example.bookstore.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    VALIDATION_FAILED("유효성 검사 실패"),
    EMAIL_ALREADY_EXISTS("이미 가입된 이메일입니다."),
    EXPIRED_TOKEN("만료된 토큰입니다."),
    INVALID_TOKEN("유효하지 않은 토큰입니다."),
    INVALID_PASSWORD("잘못된 비밀번호입니다."),
    USER_NOT_FOUND("사용자를 찾을 수 없습니다.");

    private final String message;
}
