package com.example.nagahoribashi_walk.service.userdetails;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.nagahoribashi_walk.entity.User;

/**
 * Spring Security の UserDetails 実装。
 * ログイン中ユーザーの情報をセッションに保持するクラス。
 */
public class LoginUser implements UserDetails {

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

    /** 権限のコレクションを返す */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /** ハッシュ化済みのパスワードを返す */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /** ユーザーIDを返す */
    public Long getId() {
        return user.getId();
    }

    /** 表示名を返す */
    public String getDisplayName() {
        return user.getDisplayName();
    }

    /** 保有ポイントを返す */
    public BigDecimal getPoint() {
        return user.getPoint();
    }

    /** 最後におみくじを引いた日時を返す。未設定の場合は空の Optional を返す */
    public Optional<LocalDateTime> getLastDrawnAt() {
        return Optional.ofNullable(user.getLastDrawnAt());
    }

    /** おみくじでおすすめされたスポットIDを返す。未設定の場合は空の Optional を返す */
    public Optional<Long> getRecommendedSpotId() {
        return Optional.ofNullable(user.getRecommendedSpotId());
    }

    /** ログインで利用するユーザー名を返す */
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /** アカウントが期限切れでなければ {@code true} を返す */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /** アカウントがロックされていなければ {@code true} を返す */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /** 認証情報（パスワード）が期限切れでなければ {@code true} を返す */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /** アカウントが有効であれば {@code true} を返す */
    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }
}
