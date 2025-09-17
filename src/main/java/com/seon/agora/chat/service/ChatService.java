package com.seon.agora.chat.service;

import com.seon.agora.chat.dto.ChatRequest;
import com.seon.agora.chat.dto.ChatResponse;
import com.seon.agora.common.security.UserInfo;

/**
 * @author seon
 * @version 1.0
 * @since 25. 9. 9.
 */
public interface ChatService {
    ChatResponse sendChat(ChatRequest chatRequest, UserInfo userInfo);
}
