package com.seon.agora.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-05-05
 */
@Entity
@Table(name = "users")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {
    @Id
    @Column(name= "id")
    private String id;

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "pw", nullable = false)
    private String pw;

    @Column(name= "email", nullable = false, unique = true)
    private String email;

    @Column(name= "name", nullable = false)
    private String name;

    @Column(name= "nickname", nullable = false, unique = true)
    private String nickname;

    @Column(name= "role", nullable = false)
    private String role;

    @Column(name = "create_dttm")
    private LocalDateTime createDttm;

    @Column(name = "update_dttm")
    private LocalDateTime updateDttm;

    @Column(name = "use_yn")
    private boolean useYn;

    @Column(name = "del_yn")
    private boolean delYn;

    @PrePersist
    protected void onCreate() {
        this.createDttm = this.updateDttm = LocalDateTime.now();
        this.useYn = true;
        this.delYn = false;
        this.role = "ROLE_USER";
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateDttm = LocalDateTime.now();
    }
}
