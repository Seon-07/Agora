package com.seon.fairin.auth.dto;

import lombok.Getter;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-07-20
 */
@Getter
public enum JwtToken {
    ACCESS_TOKEN("ACCESS"),
    REFRESH_TOKEN("REFRESH");

    private final String type;

    JwtToken(String type) {
        this.type = type;
    }
}
