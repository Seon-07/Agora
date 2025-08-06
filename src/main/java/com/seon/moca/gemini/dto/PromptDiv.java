package com.seon.moca.gemini.dto;

import lombok.Getter;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-06-06
 */
@Getter
public enum PromptDiv {
    intro("토론 시작 메시지"),
    after_argument("입론 후 정리"),
    after_rebuttal("반론 요약 및 다음 라운드 유도"),
    closing("결론"),
    compress("압축 및 필터"),
    validator("토론 주제 적절성 검사");

    private final String des;

    PromptDiv(String des) {
        this.des = des;
    }
}
