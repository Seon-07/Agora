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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role", referencedColumnName = "id")
    private Role role;

    @Column(name = "create_dttm")
    private LocalDateTime createDttm;

    @Column(name = "update_dttm")
    private LocalDateTime updateDttm;

    @Column(name = "use_yn")
    private String useYn;

    @Column(name = "del_yn")
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
