package com.seon.agora.auth.dto;

import lombok.Getter;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-06-08
 */
@Getter
public enum Role {
    ROLE_USER("사용자"),
    ROLE_ADMIN("관리자");

    private final String des;

    Role(String des) {
        this.des = des;
    }
}
