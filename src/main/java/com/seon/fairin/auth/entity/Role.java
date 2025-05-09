package com.seon.fairin.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-05-10
 */
@Entity
@Table(name = "role")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Role {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "role", nullable = false, unique = true)
    private String role;

    @Column(name = "des", nullable = false)
    private String des;
}
