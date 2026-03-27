package com.example.nagahoribashi_walk.service.userdetails;

import java.util.Collection;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.nagahoribashi_walk.entity.User;

public class UserDetailsImpl implements UserDetails {

	private static final long serialVersionUID = 1L;

	// ユーザー情報クラス
	private final transient User user;
	
	// 権限コレクション
	private Collection<GrantedAuthority> authorities;
	
	// コンストラクタ
	public UserDetailsImpl(
			User loginUser,
			Collection<GrantedAuthority> authorities
			) {
		this.user = loginUser;
		this.authorities = authorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// 権限のコレクションを返す
		return authorities;
	}

	@Override
	public @Nullable String getPassword() {
		// ハッシュ化済みのパスワードを返す
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// ログインで利用するユーザー名を返す
		return user.getUsername();
	}
	
	@Override
	public boolean isAccountNonExpired() {
		// ユーザーが期限切れでなければtrueを返す
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		// ユーザーがロックされていなければtrueを返す
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		// ユーザーのパスワードが期限切れでなければtrueを返す
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		// ユーザーが有効であればtrueを返す
		return this.user.isEnabled();
	}
}
