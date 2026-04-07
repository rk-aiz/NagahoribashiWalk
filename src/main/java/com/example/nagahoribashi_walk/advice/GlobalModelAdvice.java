package com.example.nagahoribashi_walk.advice;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.nagahoribashi_walk.dto.NavCategory;
import com.example.nagahoribashi_walk.service.CategoryService;
import com.example.nagahoribashi_walk.service.userdetails.LoginUser;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * グローバルに利用するModelAttributeの設定
 *
 * @author 海津
 */
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAdvice {

    private final CategoryService categoryService;

    /**
     * カテゴリナビ用のリストを取得
     */
    @ModelAttribute("navCategories")
    public List<NavCategory> navCategories(HttpServletRequest request) {
        if (request.getRequestURI().startsWith("/admin")) {
            return List.of();
        }
        return categoryService.getAllNavCategories();
    }

    /**
     * ログイン済みの場合、UserDetailsを "account" という名前でModelに追加します。
     */
    @ModelAttribute("account")
    public LoginUser addAccountToModel(
            @AuthenticationPrincipal LoginUser loginUser) {
        return loginUser;
    }
}
