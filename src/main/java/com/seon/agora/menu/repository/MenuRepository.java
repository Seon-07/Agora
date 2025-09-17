package com.seon.agora.menu.repository;

import com.seon.agora.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-06-08
 */
public interface MenuRepository extends JpaRepository<Menu, String> {
    @Query("SELECT m FROM Menu m WHERE :isAdmin = true OR m.adminOnly = false ORDER BY m.ordr")
    List<Menu> findMenuByRole(boolean isAdmin);
}
