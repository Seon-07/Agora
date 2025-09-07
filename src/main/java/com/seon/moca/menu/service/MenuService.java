package com.seon.moca.menu.service;

import com.seon.moca.common.security.UserInfo;
import com.seon.moca.menu.dto.MenuResponse;

import java.util.List;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-06-08
 */
public interface MenuService {
    List<MenuResponse> getMeusList(UserInfo userInfo);
}
