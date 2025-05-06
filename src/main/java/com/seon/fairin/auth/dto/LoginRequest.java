package com.seon.fairin.auth.dto;

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
    private String userId;
    private String pw;
}
