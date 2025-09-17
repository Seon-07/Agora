package com.seon.agora.gemini.service;

import com.seon.agora.gemini.dto.PromptDiv;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-06-01
 */
public interface GeminiService {
    String sendGemini(PromptDiv promptDiv, String message);
}
