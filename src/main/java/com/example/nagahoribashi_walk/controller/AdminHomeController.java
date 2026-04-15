package com.example.nagahoribashi_walk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.nagahoribashi_walk.service.AdminSpotService;
import com.example.nagahoribashi_walk.service.ReviewService;
import com.example.nagahoribashi_walk.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * 管理者ホーム画面・ログイン画面用コントローラー
 */
@Controller
@RequiredArgsConstructor
public class AdminHomeController {

    private final AdminSpotService adminSpotService;
    private final UserService userService;
    private final ReviewService reviewService;

    /**
     * 管理者ホーム画面を表示する
     */
    @GetMapping("/admin")
    public String home(Model model) {
        model.addAttribute("spotCount", adminSpotService.getSpotCount());
        model.addAttribute("userCount", userService.getUserCountByRole("USER"));
        model.addAttribute("avgRating", adminSpotService.getAverageRatingAll());
        model.addAttribute("reviewCount", reviewService.getReviewCount());
        model.addAttribute("recentSpots", adminSpotService.findRecent(5));
        return "admin/admin-home";
    }

    /**
     * 管理者ログイン画面を表示する
     */
    @GetMapping("/admin/login")
    public String login() {
        return "admin/admin-login";
    }

}
