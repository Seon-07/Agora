package com.seon.fairin.jwt;

import com.seon.fairin.auth.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-05-06
 */
public class UserInfo implements UserDetails {
    private final User user;

    public UserInfo(User user) {
        this.user = user;
    }

    @Override
    public String getPassword() {
        return user.getPw();
    }

    @Override
    public String getUsername() {
        return user.getUserId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(user.getRole()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return !"N".equals(user.getUseYn());
    }

    @Override
    public boolean isAccountNonLocked() {
        return !"N".equals(user.getUseYn());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !"N".equals(user.getUseYn()) && !"Y".equals(user.getDelYn());
    }
}
