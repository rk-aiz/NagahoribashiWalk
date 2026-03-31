package com.example.nagahoribashi_walk.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.nagahoribashi_walk.form.UserRegisterForm;
import com.example.nagahoribashi_walk.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * 認証関連のコントローラー
 * 
 * @author 海津
 */
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    /**
     * トップページ(ログイン画面)を表示します。
     * すでにログインしている場合はメニュー画面へリダイレクトします。
     */
    @GetMapping("/login")
    public String login(Principal principal) {

        // ログイン済みの場合はトップ画面へリダイレクトする
        if (principal != null) {
            return "redirect:/";
        }

        return "/auth/login";
    }

    @GetMapping("/register")
    public String showRegister(UserRegisterForm userRegisterForm) {
        return "/auth/register";
    }

    @PostMapping("/register")
    public String postMethodName(
        UserRegisterForm userRegisterForm
        
        ) {
        return "redirect:/register/complete";
    }
    
    @GetMapping("/register/complete")
    public String complete() {
        return "/auth/register-complete";
    }
}
