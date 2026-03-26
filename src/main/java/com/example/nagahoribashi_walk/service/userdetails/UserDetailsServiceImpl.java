package com.example.nagahoribashi_walk.service.userdetails;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.nagahoribashi_walk.entity.User;
import com.example.nagahoribashi_walk.repository.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	
	private final UserMapper userMapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		// login_userテーブルからusernameに対応するデータを取得する
		User user = userMapper
				.findByUsername(username);
		
		Collection<GrantedAuthority> authorities =
				new ArrayList<>();
		
		if (user != null) {
			authorities.add(
					new SimpleGrantedAuthority(user.getRole()));
		} else {
			throw new UsernameNotFoundException("ユーザーが存在しません");
		}
		
		return new UserDetailsImpl(user, authorities);
	}

}
