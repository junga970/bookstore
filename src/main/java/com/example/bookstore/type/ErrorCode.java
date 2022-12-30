package com.example.bookstore.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    VALIDATION_FAILED("유효성 검사 실패"),
    EMAIL_ALREADY_EXISTS("이미 가입된 이메일입니다.");

    private final String message;
}
