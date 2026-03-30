package com.example.nagahoribashi_walk.controller;

import org.springframework.stereotype.Controller;

import com.example.nagahoribashi_walk.service.SpotService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminSpotController {

    private final SpotService spotService;

}
