package com.seon.moca.common.security;

import com.seon.common.exception.ApiException;
import com.seon.common.exception.ExceptionCode;
import com.seon.moca.auth.repository.AuthRepository;
import com.seon.moca.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-05-06
 */
@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserDetailsService {

    private final AuthRepository authRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        User user = authRepository.findById(id)
                .orElseThrow(() -> new ApiException(ExceptionCode.NOT_FOUND));
        return new UserInfo(user);
    }
}
