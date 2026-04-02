package com.example.nagahoribashi_walk.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.FlashMapManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * ログアウト成功時のハンドラー
 *
 * @author 海津
 */
@Component
public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    private final FlashMapManager flashMapManager;

    public CustomLogoutSuccessHandler(FlashMapManager flashMapManager) {
        this.flashMapManager = flashMapManager;
        setDefaultTargetUrl("/");
    }

    // FlashAttributeで通知を渡す
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        FlashMap flashMap = new FlashMap();
        flashMap.put("logoutSuccess", true);
        flashMapManager.saveOutputFlashMap(flashMap, request, response);

        super.onLogoutSuccess(request, response, authentication);
    }
}
