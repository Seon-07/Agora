package com.seon.fairin.auth.repository;

import com.seon.fairin.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-05-10
 */
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByRole(String role);
}
