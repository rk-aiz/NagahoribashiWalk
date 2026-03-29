package com.example.nagahoribashi_walk.controller;

import org.springframework.stereotype.Controller;

import com.example.nagahoribashi_walk.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminReviewController {

    private final ReviewService reviewService;

}
