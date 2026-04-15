package com.example.nagahoribashi_walk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * エラーページ用コントローラー
 * Spring Securityの accessDeniedPage から転送される
 */
@Controller
public class ErrorPageController {

    /**
     * アクセス拒否（403）画面を表示する
     */
    @GetMapping("/403")
    public String accessDenied() {
        return "error/403";
    }
}
