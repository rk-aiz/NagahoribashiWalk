package com.example.nagahoribashi_walk.config;

import org.springframework.boot.security.autoconfigure.web.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

/**
 * Spring Securityの設定
 *
 * @author 海津
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomLogoutSuccessHandler logoutSuccessHandler;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) {

        http
                // ★HTTPリクエストに対するセキュリティ設定
                .authorizeHttpRequests(authz -> authz
                        // 「ホーム、スポット閲覧系、ログイン、登録画面、エラー画面」へのアクセスは認証を必要としない
                        .requestMatchers("/", "/about", "/spot/**", "/login", "/register", "/register/**", "/error", "/error/**").permitAll()
                        // static以下の"/css/**" "/js/**" "/images/**" "/fonts/**" "/favicon.* などは認証を必要としない
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        // アップロードリソースは認証を必要としない
                        .requestMatchers("/uploads/**").permitAll()
                        // 【管理者権限設定】 url : /admin/**は管理者しかアクセスできない
                        .requestMatchers("/admin", "/admin/**").permitAll()
                        // ★その他のリクエストはすべて認証が必要
                        .anyRequest().authenticated())

                // ★フォームによるログイン設定
                .formLogin(form -> form
                        // ログイン画面のURL
                        .loginPage("/login")
                        // ログイン処理のURLを指定
                        .loginProcessingUrl("/authentication")
                        // ユーザー名のname属性を指定
                        .usernameParameter("username")
                        // パスワードのname属性を指定
                        .passwordParameter("password")
                        // ログイン成功時のURLを指定
                        .defaultSuccessUrl("/")
                        // ログイン失敗時のURLを指定
                        .failureUrl("/login?error"))

                // ★ログアウト設定
                .logout(logout -> logout
                        // ログアウトを処理するURLを指定
                        .logoutUrl("/logout")
                        // ログアウト成功時の処理（FlashAttributeで通知）
                        .logoutSuccessHandler(logoutSuccessHandler)
                        // ログアウト時にセッションを無効にする
                        .invalidateHttpSession(true)
                        // ログアウト時にCookieを削除する
                        .deleteCookies("JSESSIONID"));

        return http.build();
    }
}
