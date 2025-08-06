package com.seon.moca.menu.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-06-08
 */
@Getter
@Builder
public class MenuResponse {
    private String menuName;

    private String uri;

    private String icon;
}
