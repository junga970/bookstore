package com.example.bookstore.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegister {
    @NotBlank
    private String name;

    @NotBlank
    @Pattern(regexp = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", message = "잘못된 이메일 형식입니다.")
    private String email;

    @NotBlank
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "잘못된 비밀번호 형식입니다. 영문/숫자/특수문자 3가지 이상 조합 (8자 이상)"
    )
    private String password;

    @NotBlank
    @Pattern(regexp = "^01(?:0|1)?(\\d{3}|\\d{4})?(\\d{4})$", message = "잘못된 휴대폰 번호 형식입니다.")
    private String phone;

    private String address;
}
