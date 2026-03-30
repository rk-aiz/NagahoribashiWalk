package com.example.nagahoribashi_walk.service.userdetails;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagahoribashi_walk.entity.User;
import com.example.nagahoribashi_walk.repository.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // usersテーブルからusernameに対応するデータを取得する
        User user = userMapper.findByUsername(username).orElseThrow(() -> {
            throw new UsernameNotFoundException(
                    username + " => 指定しているユーザー名は存在しません");
        });

        if (!user.isEnabled()) {
            throw new DisabledException(
                    username + " => 指定しているユーザーは有効ではありません");
        }

        Collection<GrantedAuthority> authorities = new ArrayList<>();

        return new UserDetailsImpl(user, authorities);
    }

}
