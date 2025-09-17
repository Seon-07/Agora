package com.seon.agora.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-06-06
 */
@Component
public class PromptLoader {
    private Map<String, String> prompts;

    @PostConstruct
    public void init() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ClassPathResource resource = new ClassPathResource("prompts.json");
        prompts = mapper.readValue(resource.getInputStream(), new TypeReference<>() {});
    }

    public String getPrompt(String key) {
        return prompts.get(key);
    }
}
