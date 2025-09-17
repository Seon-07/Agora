package com.seon.agora.debate.dto;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-09-06
 */
public enum DebateActionType {
    WAIT("대기"),
    PRESENT("입론"),
    REBUTTAL("반론"),
    SUMMARY("정리"),
    REVIEW("요약"),
    PROGRESS("진행"),
    FORFEIT("포기"),
    ABORT("중단");

    private final String des;

    DebateActionType(String des) {
        this.des = des;
    }
}
