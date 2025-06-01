package com.seon.fairin.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-05-06
 */
@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "아이디를 입력해주세요.")
    private String userId;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String pw;
}
