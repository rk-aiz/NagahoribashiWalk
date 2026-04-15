package com.example.nagahoribashi_walk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * "このサイトについて"ページ用コントローラー
 */
@Controller
public class AboutController {

    /**
     * "このサイトについて"ページを表示
     */
    @GetMapping("/about")
    public String showAbout() {
        return "about";
    }
}
