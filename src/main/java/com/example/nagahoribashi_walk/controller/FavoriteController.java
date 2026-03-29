package com.example.nagahoribashi_walk.controller;

import org.springframework.stereotype.Controller;

import com.example.nagahoribashi_walk.service.FavoriteService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

}
