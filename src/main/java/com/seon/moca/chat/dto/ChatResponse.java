package com.seon.moca.chat.dto;

import com.seon.moca.chat.entity.Chat;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * @author seon
 * @version 1.0
 * @since 25. 9. 8.
 */
@Getter
public class ChatResponse {
    private String id;

    private String roomId;

    private String message;

    private String senderId;

    private String senderNickname;

    private LocalDateTime sendDttm;

    private SenderType senderType;

    public ChatResponse(Chat chat, String senderNickname){
        this.id = chat.getId();
        this.roomId = chat.getRoom().getId();
        this.message = chat.getMessage();
        this.senderId = chat.getSender().getId();
        this.senderNickname = senderNickname;
        this.sendDttm = chat.getSendDttm();
        this.senderType = chat.getSenderType();
    }
}
