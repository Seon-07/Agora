package com.seon.fairin.menu.service;

import com.seon.fairin.auth.dto.Role;
import com.seon.fairin.jwt.UserInfo;
import com.seon.fairin.menu.dto.MenuResponse;
import com.seon.fairin.menu.entity.Menu;
import com.seon.fairin.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-06-08
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MenuServiceImpl implements MenuService {
    private final MenuRepository menuRepository;

    /**
     * 사용자에 따른 메뉴리스트 가져오기
     * - 관리자/ 사용자
     */
    public List<MenuResponse> getMeusList(UserInfo userInfo){
        boolean isAdmin = userInfo.getRole().equals(Role.ROLE_ADMIN.toString());
        List<Menu> menuList = menuRepository.findMenuByRole(isAdmin);
        return menuList.stream()
                .map(this::toMenuResponse)
                .collect(Collectors.toList());
    }

    /**
     * Entity -> DTO
     */
    private MenuResponse toMenuResponse(Menu menu) {
        return MenuResponse.builder()
                .menuName(menu.getMenuName())
                .uri(menu.getUri())
                .icon(menu.getIcon())
                .build();
    }
}
