package com.seon.fairin.gemini.service;

import com.seon.fairin.gemini.dto.PromptDiv;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-06-01
 */
public interface GeminiService {
    String sendGemini(PromptDiv promptDiv, String message);
}
