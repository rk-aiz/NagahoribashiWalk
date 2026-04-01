package com.example.nagahoribashi_walk.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.nagahoribashi_walk.service.FavoriteService;

import lombok.RequiredArgsConstructor;

@Controller
//@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;
    
//    @PostMapping("/favorite")
//    public String favorite(@RequestParam Long spotId,
//                           Principal principal) {
//
//        String userId = principal.getName();
//
//        favoriteService.addFavorite(userId, spotId);
//
//        return "redirect:/spot/" + spotId;
    }
}
