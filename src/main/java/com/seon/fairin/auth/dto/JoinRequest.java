package com.seon.fairin.auth.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-05-06
 */
@Getter
@Setter
public class JoinRequest {
    private String userId;

    private String pw;

    private String email;

    private String name;

    private String nickname;
}
