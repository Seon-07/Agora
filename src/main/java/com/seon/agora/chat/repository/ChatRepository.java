package com.seon.agora.chat.repository;

import com.seon.agora.chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author seon
 * @version 1.0
 * @since 25. 9. 9.
 */
public interface ChatRepository extends JpaRepository<Chat, String> {
}
