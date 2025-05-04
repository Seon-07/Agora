package com.seon.fairin.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import java.time.LocalDateTime;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-05-05
 */
@Entity
@Table(name = "users")
@Getter
public class User {
    @Id
    @Column(length = 32)
    private String id;

    @Column(name = "user_id", length = 20, nullable = false, unique = true)
    private String userId;

    @Column(length = 255, nullable = false, unique = true)
    private String email;

    @Column(length = 30, nullable = false)
    private String name;

    @Column(length = 30, nullable = false, unique = true)
    private String nickname;

    @Column(length = 32, nullable = false)
    private String role;

    @Column(name = "create_dttm")
    private LocalDateTime createDttm;

    @Column(name = "use_yn", length = 1)
    private String useYn;

    @Column(name = "del_yn", length = 1)
    private String delYn;
}
