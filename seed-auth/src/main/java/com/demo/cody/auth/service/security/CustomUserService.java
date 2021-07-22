package com.demo.cody.auth.service.security;

import cn.hutool.core.util.IdUtil;
import com.demo.cody.auth.constant.MessageConstant;
import com.demo.cody.auth.entity.SecurityUser;
import com.demo.cody.common.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
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

    List<SysUser> list;
    private List<User> userList;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        String password = passwordEncoder.encode("123456");
        list = new ArrayList<>();
        list.add(SysUser.builder().userName("admin").password(password).enabled(Boolean.TRUE).id(IdUtil.objectId()).build());
        list.add(SysUser.builder().userName("test").password(password).enabled(Boolean.TRUE).id(IdUtil.objectId()).build());

        userList = new ArrayList<>();
        userList.add(new User("macro", password, AuthorityUtils.commaSeparatedStringToAuthorityList("admin")));
        userList.add(new User("andy", password, AuthorityUtils.commaSeparatedStringToAuthorityList("client")));
        userList.add(new User("mark", password, AuthorityUtils.commaSeparatedStringToAuthorityList("client")));
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        // TODO 模拟用户查询
        List<SysUser> user = list.stream().filter(f -> f.getUserName().equals(s)).collect(Collectors.toList());
        if (user.size() == 0) {
            throw new UsernameNotFoundException(MessageConstant.USERNAME_PASSWORD_ERROR);
        }
        // 用户权限列表，根据用户拥有的权限标识与如 @PreAuthorize("hasAuthority('sys:menu:view')") 标注的接口对比，决定是否可以调用接口
        // List<String> permissions = sysMenuService.getPermissionsByUserId(user.getId());
        // log.info("用户权限标识 permissions = {}", permissions);
        // List<GrantedAuthority> grantedAuthorities = permissions.stream().map(GrantedAuthorityImpl::new).collect(Collectors.toList());
        //return new JwtUserDetails(username, user.getPassword(), user.getEnabled(), grantedAuthorities);
        // TODO 用户权限查询
        SysUser findUser = user.get(0);
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


        /*List<User> findUserList = userList.stream().filter(f -> f.getUsername().equals(s)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(findUserList)) {
            return findUserList.get(0);
        } else {
            throw new UsernameNotFoundException("用户名或密码错误");
        }*/
    }

}
