package com.example.nagahoribashi_walk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // ★HTTPリクエストに対するセキュリティ設定
                .authorizeHttpRequests(authz -> authz
                		
                		
                		.anyRequest().permitAll()
                		/*
                        // 「ホーム、ログイン、登録画面、favicon.svg」へのアクセスは認証を必要としない
                        .requestMatchers("/", "/spot/**", "/user/login", "/user/register").permitAll()
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                        .permitAll()
                        // 【管理者権限設定】 url : /admin/**は管理者しかアクセスできない
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        // ★その他のリクエストはすべて認証が必要
                        .anyRequest().authenticated()*/)

                // ★フォームによるログイン設定
                .formLogin(form -> form
                        // ログイン画面のURL
                        .loginPage("/login")
                        // ログイン処理のURLを指定
                        .loginProcessingUrl("/authentication")
                        // ユーザー名のname属性を指定
                        .usernameParameter("usernameInput")
                        // パスワードのname属性を指定
                        .passwordParameter("passwordInput")
                        // ログイン成功時のURLを指定
                        .defaultSuccessUrl("/")
                        // ログイン失敗時のURLを指定
                        .failureUrl("/login?error"))

                // ★ログアウト設定
                .logout(logout -> logout
                        // ログアウトを処理するURLを指定
                        .logoutUrl("/logout")
                        // ログアウト時にセッションを無効にする
                        .invalidateHttpSession(true)
                        // ログアウト時にCookieを削除する
                        .deleteCookies("JSESSIONID"));

        return http.build();
    }
}