package com.seon.agora.common.security;

import com.seon.agora.user.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-05-06
 */
@Getter
public class UserInfo implements UserDetails, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String id;
    private final String userId;
    private final String name;
    private final String pw;
    private final String nickname;
    private final String role;
    private final boolean useYn;
    private final boolean delYn;

    public UserInfo(User user) {
        this.id = user.getId();
        this.userId = user.getUserId();
        this.name = user.getName();
        this.pw = user.getPw();
        this.nickname = user.getNickname();
        this.role = user.getRole();
        this.useYn = user.isUseYn();
        this.delYn = user.isDelYn();
    }

    @Override
    public String getPassword() {
        return pw;
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return useYn;
    }

    @Override
    public boolean isAccountNonLocked() {
        return useYn;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return useYn && !delYn;
    }


}
