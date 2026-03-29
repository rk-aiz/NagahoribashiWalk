package com.example.nagahoribashi_walk.controller;

import org.springframework.stereotype.Controller;

import com.example.nagahoribashi_walk.service.SpotService;

import lombok.RequiredArgsConstructor;

/**
 * @author 海津
 */
@Controller
@RequiredArgsConstructor
public class SpotController {

    private final SpotService spotService;

}
