package com.seon.fairin.menu.service;

import com.seon.fairin.jwt.UserInfo;
import com.seon.fairin.menu.dto.MenuResponse;

import java.util.List;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-06-08
 */
public interface MenuService {
    List<MenuResponse> getMeusList(UserInfo userInfo);
}
