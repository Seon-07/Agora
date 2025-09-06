package com.seon.moca.jwt;

import com.seon.moca.user.entity.User;
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
        return user.isUseYn();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isUseYn();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isUseYn() && !user.isDelYn();
    }

    public User getUser() {return user;}

    public String getId() {
        return user.getId();
    }

    public String getNickname() {
        return user.getNickname();
    }

    public String getRole(){
        return user.getRole();
    }
}
