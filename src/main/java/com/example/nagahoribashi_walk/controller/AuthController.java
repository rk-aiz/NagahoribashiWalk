package com.example.nagahoribashi_walk.controller;

import java.security.Principal;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.nagahoribashi_walk.entity.User;
import com.example.nagahoribashi_walk.exception.UserAlreadyExistsException;
import com.example.nagahoribashi_walk.form.UserRegisterForm;
import com.example.nagahoribashi_walk.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 認証関連のコントローラー
 * 
 * @author 海津
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    /**
     * ログイン画面を表示する
     * すでにログインしている場合はメニュー画面へリダイレクトする
     */
    @GetMapping("/login")
    public String login(Principal principal) {

        // ログイン済みの場合はトップ画面へリダイレクトする
        if (principal != null) {
            return "redirect:/";
        }

        return "/auth/login";
    }

    /**
     * 新規会員登録画面を表示
     */
    @GetMapping("/register")
    public String showRegister(UserRegisterForm userRegisterForm) {
        return "/auth/register";
    }

    /**
     * 新規会員登録を実行
     */
    @PostMapping("/register")
    public String register(
            @Validated UserRegisterForm userRegisterForm,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "/auth/register";
        }

        User newUser = new User();
        BeanUtils.copyProperties(userRegisterForm, newUser, "password");
        newUser.setRole("USER");

        try {
            userService.register(newUser, userRegisterForm.getPassword());
        } catch (UserAlreadyExistsException e) {
            model.addAttribute("errorMessage", e.getLocalizedMessage());
            return "/auth/register";
        }

        log.info(String.format(
                "新規会員 : %s", userRegisterForm));

        return "redirect:/register/complete";
    }

    /**
     * 新規会員登録完了画面を表示
     */
    @GetMapping("/register/complete")
    public String complete() {
        return "/auth/register-complete";
    }
}
