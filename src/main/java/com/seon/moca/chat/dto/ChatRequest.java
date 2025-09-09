package com.seon.moca.chat.dto;

import lombok.Getter;

/**
 * @author seon
 * @version 1.0
 * @since 25. 9. 8.
 */
@Getter
public class ChatRequest {
    private String roomId;

    private String message;
}
