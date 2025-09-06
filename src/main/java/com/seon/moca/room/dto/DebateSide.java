package com.seon.moca.room.dto;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-09-06
 */
public enum DebateSide {
    PRO("찬성측"),
    CON("반대측");

    private final String des;

    DebateSide(String des) {
        this.des = des;
    }
}
