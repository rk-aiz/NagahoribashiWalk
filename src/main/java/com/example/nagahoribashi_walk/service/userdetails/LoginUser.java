package com.example.nagahoribashi_walk.service.userdetails;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.nagahoribashi_walk.entity.User;

public class LoginUser implements UserDetails, Serializable {

    private static final long serialVersionUID = 1L;

    // ユーザー情報クラス
    private final User user;

    // 権限コレクション
    private Collection<GrantedAuthority> authorities;

    // コンストラクタ
    public LoginUser(
            User loginUser,
            Collection<GrantedAuthority> authorities) {
        this.user = Objects.requireNonNull(loginUser, "loginUser must not be null");
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 権限のコレクションを返す
        return authorities;
    }

    @Override
    public String getPassword() {
        // ハッシュ化済みのパスワードを返す
        return user.getPassword();
    }

    public Long getId() {
        return user.getId();
    }

    public String getDisplayName() {
        return user.getDisplayName();
    }

    public BigDecimal getPoint() {
        return user.getPoint();
    }

    public Optional<LocalDateTime> getLastDrawnAt() {
        return Optional.ofNullable(user.getLastDrawnAt());
    }

    public Optional<Long> getRecommendedSpotId() {
        return Optional.ofNullable(user.getRecommendedSpotId());
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
        return user.isEnabled();
    }
}
