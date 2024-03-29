package com.duofan.starter.security;

import com.duofan.starter.dto.model.common.UserDto;
import com.duofan.starter.model.enums.UserRole;
import com.duofan.starter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Arpit Khandelwal.
 */
@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;


    // 查看是否有改用户，没有则抛出异常
    // 如果有则传给框架，也就是返回UserDetails
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto userDto = null;
        try {
            userDto = userService.findUserByUsername(username);
            List<GrantedAuthority> authorities = getUserAuthority(userDto.getRole());
            return buildUserForAuthentication(userDto, authorities);
        } catch (RuntimeException e) {
            throw new UsernameNotFoundException("用户名 " + username + " 不存在。");
        }
    }

    private List<GrantedAuthority> getUserAuthority(UserRole role) {
        Set<GrantedAuthority> roles = new HashSet<>();
        roles.add(new SimpleGrantedAuthority(role.toString()));
        return new ArrayList<GrantedAuthority>(roles);
    }

    private UserDetails buildUserForAuthentication(UserDto user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }


}
