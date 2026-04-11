package com.example.nagahoribashi_walk.config;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * 認証要求時に元のURLを保持するためのカスタムエントリーポイント
 * CustomAuthenticationSuccessHandlerで使用
 * 
 * @author 海津
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException {

        // Refererヘッダー（直前のページURL）を取得
        String referer = request.getHeader("Referer");

        String loginUrl;
        if (referer != null && !referer.isEmpty()) {
            // 詳細ページのURLをreturnUrlとして渡す
            loginUrl = "/login?returnUrl=" + URLEncoder.encode(referer, StandardCharsets.UTF_8);
        } else {
            loginUrl = "/login";
        }

        response.sendRedirect(loginUrl);
    }
}
