package com.example.nagahoribashi_walk.config;

import org.springframework.boot.security.autoconfigure.web.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
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

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomLogoutSuccessHandler logoutSuccessHandler;

    // =========================================================
    // 管理者用 SecurityFilterChain
    // 適用範囲: /admin および /admin/** のリクエストのみ
    // @Order(1) により、一般ユーザー用より先に評価される
    // =========================================================
    @Bean
    @Order(1)
    SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
        http
            // このFilterChainは /admin と /admin/** にのみ適用する
            // それ以外のURLは @Order(2) の userFilterChain が処理する
            .securityMatcher("/admin", "/admin/**")

            // ★アクセス制御
            .authorizeHttpRequests(authz -> authz
                // 管理者ログイン画面は未認証でもアクセス可能
                .requestMatchers("/admin/login").permitAll()
                // 上記以外の /admin/** は ADMIN 権限必須
                // ADMIN 以外のユーザーはここで 403 になる
                .anyRequest().hasRole("ADMIN"))

            // ★ログイン設定
            .formLogin(form -> form
                // 管理者専用ログイン画面のURL
                .loginPage("/admin/login")
                // ログインフォームの送信先URL（Spring Securityが処理）
                .loginProcessingUrl("/admin/authentication")
                // フォームの name 属性に合わせる（デフォルトも "username" だが明示）
                .usernameParameter("username")
                // フォームの name 属性に合わせる（デフォルトも "password" だが明示）
                .passwordParameter("password")
                // ログイン成功時は管理者ダッシュボードへ
                // hasAuthority("ADMIN") で弾かれるため、ここに到達するのはADMINのみ
                .defaultSuccessUrl("/admin")
                // ログイン失敗時は管理者ログイン画面にエラーパラメーター付きで戻る
                .failureUrl("/admin/login?error"))

            // ★ログアウト設定
            .logout(logout -> logout
                // 管理者用ログアウトURL（一般ユーザーの /logout とは別）
                .logoutUrl("/admin/logout")
                // ログアウト後は管理者ログイン画面へ
                .logoutSuccessUrl("/admin/login")
                // ログアウト時にセッションを破棄する
                .invalidateHttpSession(true)
                // ログアウト時にセッションCookieを削除する
                .deleteCookies("JSESSIONID"))

            // ★403処理: ADMIN以外の認証済みユーザーが /admin/** にアクセスした場合
            .exceptionHandling(ex -> ex
                .accessDeniedPage("/admin/login?error=forbidden"));

        return http.build();
    }

    // =========================================================
    // 一般ユーザー用 SecurityFilterChain
    // 適用範囲: /admin/** 以外の全リクエスト
    // @Order(2) により、管理者用の後に評価される
    // =========================================================
    @Bean
    @Order(2)
    SecurityFilterChain userFilterChain(HttpSecurity http) throws Exception {
        http
            // ★アクセス制御
            .authorizeHttpRequests(authz -> authz
                // 認証不要のURL（非ログインユーザーも閲覧可能）
                .requestMatchers("/", "/about", "/spot/**", "/login", "/register", "/register/**",
                        "/error", "/error/**", "/403").permitAll()
                // CSS・JS・画像などの静的リソースは認証不要
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                // アップロード画像も認証不要
                .requestMatchers("/uploads/**").permitAll()
                // マイページ・お気に入り・レビューは USER 権限必須
                // ADMIN はこれらのページにアクセスできない（403になる）
                .requestMatchers("/mypage/**", "/favorite/**", "/review/**").hasRole("USER")
                // 上記以外のURLはログイン済みであればアクセス可能
                .anyRequest().authenticated())

            // ★ログイン設定
            .formLogin(form -> form
                // ログイン画面のURL
                //.loginPage("/login")
                // ログインフォームの送信先URL（Spring Securityが処理）
                .loginProcessingUrl("/authentication")
                // フォームの name 属性に合わせる（デフォルトも "username" だが明示）
                .usernameParameter("username")
                // フォームの name 属性に合わせる（デフォルトも "password" だが明示）
                .passwordParameter("password")
                // ログイン成功時はトップページへ
                .successHandler(customAuthenticationSuccessHandler) // ← 追加
                // ログイン失敗時はログイン画面にエラーパラメーター付きで戻る
                .failureUrl("/login?error"))

            // ★ログアウト設定
            .logout(logout -> logout
                // ログアウト処理のURL
                .logoutUrl("/logout")
                // ログアウト後の処理（FlashAttributeでログアウト通知を渡す）
                .logoutSuccessHandler(logoutSuccessHandler)
                // ログアウト時にセッションを破棄する
                .invalidateHttpSession(true)
                // ログアウト時にセッションCookieを削除する
                .deleteCookies("JSESSIONID"))

            // ★403処理: ADMINが /mypage/** など一般ユーザー専用ページにアクセスした場合
            .exceptionHandling(ex -> ex
            	.authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedPage("/403")
                );

        return http.build();
    }
}

