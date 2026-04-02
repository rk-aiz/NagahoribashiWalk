package com.example.nagahoribashi_walk.service.userdetails;

import java.io.Serializable;
import java.util.Collection;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.nagahoribashi_walk.entity.User;

public class LoginUser implements UserDetails, Serializable  {

	private static final long serialVersionUID = 1L;

	// ユーザー情報クラス
	private final User user;
	
	// 権限コレクション
	private Collection<GrantedAuthority> authorities;
	
	// コンストラクタ
	public LoginUser(
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
		if (this.user == null) return null;
		
		return user.getPassword();
	}

	public Long getId() {
		if (this.user == null) return null;
		
		return this.user.getId();
	}
	
	@Override
	public String getUsername() {
		if (this.user == null) return null;
		
		// ログインで利用するユーザー名を返す
		return user.getUsername();
	}
	
	@Override
	public boolean isAccountNonExpired() {
		if (this.user == null) return false;
		
		// ユーザーが期限切れでなければtrueを返す
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		if (this.user == null) return false;
		// ユーザーがロックされていなければtrueを返す
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		if (this.user == null) return false;
		// ユーザーのパスワードが期限切れでなければtrueを返す
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		if (this.user == null) return false;
		// ユーザーが有効であればtrueを返す
		return this.user.isEnabled();
	}
}
