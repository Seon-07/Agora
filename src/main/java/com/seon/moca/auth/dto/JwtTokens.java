package com.seon.moca.auth.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-05-15
 */
@Builder
@Getter
public class JwtTokens {
    private String accessToken;
    private String refreshToken;
}
