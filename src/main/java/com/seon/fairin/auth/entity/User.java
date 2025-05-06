package com.seon.fairin.auth.entity;

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
    @Column(name= "id",length = 32)
    private String id;

    @Column(name = "user_id", length = 20, nullable = false, unique = true)
    private String userId;

    @Column(name = "pw", nullable = false)
    private String pw;

    @Column(name= "email", nullable = false, unique = true)
    private String email;

    @Column(name= "name",length = 30, nullable = false)
    private String name;

    @Column(name= "nickname",length = 30, nullable = false, unique = true)
    private String nickname;

    @Column(name= "role",length = 32, nullable = false)
    private String role;

    @Column(name = "create_dttm")
    private LocalDateTime createDttm;

    @Column(name = "update_dttm")
    private LocalDateTime updateDttm;

    @Column(name = "use_yn", length = 1)
    private String useYn;

    @Column(name = "del_yn", length = 1)
    private String delYn;

    @PrePersist
    protected void onCreate() {
        this.createDttm = this.updateDttm = LocalDateTime.now();
        this.useYn = this.useYn == null ? "Y" : this.useYn;
        this.delYn = this.delYn == null ? "N" : this.delYn;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateDttm = LocalDateTime.now();
    }
}
