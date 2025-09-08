package com.seon.moca.chat.dto;

import com.seon.moca.room.dto.DebateSide;
import com.seon.moca.user.entity.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * @author seon
 * @version 1.0
 * @since 25. 9. 8.
 */
public class ChatRequest {
    private String roomId;

    private String message;

    private String senderId;

    private DebateSide side;
}
