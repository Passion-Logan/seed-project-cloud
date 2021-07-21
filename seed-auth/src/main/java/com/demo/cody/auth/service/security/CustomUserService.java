package com.demo.cody.auth.service.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 *
 * ClassName: CustomUserService
 *
 * @author WQL
 * @Description:
 * @date: 2021/7/11 18:00
 * @since JDK 1.8
 */
public class CustomUserService implements UserDetailsService {


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return null;
    }
}
