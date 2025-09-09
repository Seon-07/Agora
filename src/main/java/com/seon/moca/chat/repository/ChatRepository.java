package com.seon.moca.chat.repository;

import com.seon.moca.chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author seon
 * @version 1.0
 * @since 25. 9. 9.
 */
public interface ChatRepository extends JpaRepository<Chat, String> {
}
