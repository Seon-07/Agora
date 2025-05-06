package com.seon.fairin.auth.repository;

import com.seon.fairin.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-05-06
 */
public interface AuthRepository extends JpaRepository<User, String> {
    Optional<User> findByUserId(String userId);

    @Query("SELECT u FROM User u WHERE u.userId = :userId OR u.email = :email OR u.nickname = :nickname")
    List<User> findDuplicates(@Param("userId") String userId, @Param("email") String email, @Param("nickname") String nickname);
}
