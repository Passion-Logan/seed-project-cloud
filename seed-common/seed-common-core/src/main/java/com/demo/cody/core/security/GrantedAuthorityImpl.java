package com.demo.cody.core.security;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author Administrator
 * @Description: GrantedAuthorityImpl
 * @date: 2020年06月16日 18:49
 */
public class GrantedAuthorityImpl implements GrantedAuthority {
    private static final long serialVersionUID = 1L;

    private String authority;

    public GrantedAuthorityImpl(String authority) {
        this.authority = authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }

}
