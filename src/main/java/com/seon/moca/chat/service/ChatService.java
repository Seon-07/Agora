package com.seon.moca.chat.service;

import com.seon.moca.chat.dto.ChatRequest;
import com.seon.moca.chat.dto.ChatResponse;
import com.seon.moca.common.security.UserInfo;

/**
 * @author seon
 * @version 1.0
 * @since 25. 9. 9.
 */
public interface ChatService {
    ChatResponse sendChat(ChatRequest chatRequest, UserInfo userInfo);
}
