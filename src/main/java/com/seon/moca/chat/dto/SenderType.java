package com.seon.moca.chat.dto;

/**
 * @author seon
 * @version 1.0
 * @since 25. 9. 8.
 */
public enum SenderType {
    PRO("찬성"),
    CON("반대"),
    MOD("사회자"),
    SYS("시스템");

    private final String des;

    SenderType(String des) {
        this.des = des;
    }
}
