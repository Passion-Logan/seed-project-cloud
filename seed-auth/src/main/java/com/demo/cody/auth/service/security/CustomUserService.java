package com.demo.cody.auth.service.security;

import com.demo.cody.auth.constant.MessageConstant;
import com.demo.cody.auth.entity.SecurityUser;
import com.demo.cody.auth.feign.SystemService;
import com.demo.cody.model.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * ClassName: CustomUserService
 *
 * @author WQL
 * @Description:
 * @date: 2021/7/11 18:00
 * @since JDK 1.8
 */
@Slf4j
@Service
public class CustomUserService implements UserDetailsService {

    @Resource
    private SystemService systemService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        // 用户权限列表，根据用户拥有的权限标识与如 @PreAuthorize("hasAuthority('sys:menu:view')") 标注的接口对比，决定是否可以调用接口
        // List<String> permissions = sysMenuService.getPermissionsByUserId(user.getId());
        // log.info("用户权限标识 permissions = {}", permissions);
        // List<GrantedAuthority> grantedAuthorities = permissions.stream().map(GrantedAuthorityImpl::new).collect(Collectors.toList());
        //return new JwtUserDetails(username, user.getPassword(), user.getEnabled(), grantedAuthorities);

        SysUser findUser = systemService.findByUsername(s);
        log.info("======= {}", findUser);
        if (Objects.isNull(findUser)) {
            throw new UsernameNotFoundException(MessageConstant.USERNAME_PASSWORD_ERROR);
        }

//        List<String> permissions = systemService.getPermissionsByUserId(findUser.getId());
//        log.info("用户权限标识 permissions = {}", permissions);
//        List<SimpleGrantedAuthority> grantedAuthorities = permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        SecurityUser securityUser = new SecurityUser(findUser);

        if (!securityUser.getEnabled()) {
            throw new DisabledException(MessageConstant.ACCOUNT_DISABLED);
        } else if (!securityUser.isAccountNonLocked()) {
            throw new LockedException(MessageConstant.ACCOUNT_LOCKED);
        } else if (!securityUser.isAccountNonExpired()) {
            throw new AccountExpiredException(MessageConstant.ACCOUNT_EXPIRED);
        } else if (!securityUser.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException(MessageConstant.CREDENTIALS_EXPIRED);
        }
        return securityUser;
    }

}
