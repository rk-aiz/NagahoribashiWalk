package com.example.nagahoribashi_walk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {

    //private final SpotService spotService;

    @GetMapping("/")
    public String showHome() {
        return "home";
    }

}
