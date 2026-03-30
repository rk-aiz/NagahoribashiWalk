package com.example.nagahoribashi_walk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SpotController {

    //private final SpotService spotService;

    @GetMapping("/spot/category/all")
    public String list() {
        return "spot/list";
    }

}
