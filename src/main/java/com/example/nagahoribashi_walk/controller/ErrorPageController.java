package com.example.nagahoribashi_walk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * エラーページ用コントローラー
 * Spring Securityの accessDeniedPage から転送される
 */
@Controller
public class ErrorPageController {

    @GetMapping("/403")
    public String accessDenied() {
        return "error/403";
    }
}
