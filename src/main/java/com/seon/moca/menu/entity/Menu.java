package com.seon.moca.menu.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-06-08
 */
@Entity
@Table(name = "menus")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Menu {
    @Id
    @Column(name= "id")
    private String id;

    @Column(name = "menu_name", nullable = false)
    private String menuName;

    @Column(name = "uri", nullable = false)
    private String uri;

    @Column(name = "admin_only", nullable = false)
    private boolean adminOnly;

    @Column(name = "ordr", nullable = false)
    private int ordr;

    @Column(name = "create_dttm", nullable = false)
    private LocalDateTime createDttm;

    @Column(name = "icon", nullable = false)
    private String icon;

    @PrePersist
    protected void onCreate() {
        this.createDttm = LocalDateTime.now();
    }
}
