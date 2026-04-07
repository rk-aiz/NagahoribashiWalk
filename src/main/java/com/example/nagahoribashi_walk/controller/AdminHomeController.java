package com.example.nagahoribashi_walk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.nagahoribashi_walk.repository.ReviewMapper;
import com.example.nagahoribashi_walk.repository.SpotMapper;
import com.example.nagahoribashi_walk.repository.UserMapper;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminHomeController {

    private final SpotMapper spotMapper;
    private final UserMapper userMapper;
    private final ReviewMapper reviewMapper;

    @GetMapping("/admin")
    public String home(Model model) {
        model.addAttribute("spotCount", spotMapper.count());
        model.addAttribute("userCount", userMapper.count());
        model.addAttribute("avgRating", spotMapper.findAverageRatingAll());
        model.addAttribute("reviewCount", reviewMapper.count());
        model.addAttribute("recentSpots", spotMapper.findRecent(5));
        return "admin/admin-home";
    }

    @GetMapping("/admin/login")
    public String login() {
        return "admin/admin-login";
    }

}
