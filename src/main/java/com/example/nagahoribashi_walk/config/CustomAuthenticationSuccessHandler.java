package com.example.nagahoribashi_walk.config;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * ログイン成功時のカスタムハンドラ
 * 
 * @author 海津
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    /**
     * ログイン成功時に、ログインページへ飛んだ元のURL(ページ)へリダイレクトする。
     * 例: スポット詳細ページ -> レビュー投稿したい -> ログイン -> 自動で再びスポット詳細ページ
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

        String returnUrl = request.getParameter("returnUrl");

        if (returnUrl != null && !returnUrl.isEmpty()) {
            response.sendRedirect(returnUrl);
        } else {
            response.sendRedirect("/");
        }
    }
}
