package com.example.nagahoribashi_walk.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.nagahoribashi_walk.service.FavoriteService;
import com.example.nagahoribashi_walk.service.userdetails.LoginUser;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService ;
    
    @PostMapping("/favorite/remove")
    public String favoriteRemove(@RequestParam Long spotId,
    		@AuthenticationPrincipal LoginUser loginUser) {

        Long userId = loginUser.getId();

        favoriteService.removeFavorite(userId, spotId);

        return "redirect:/spot/" + spotId;
    }
    
    @PostMapping("/favorite/add")
    public String favoriteAdd(@RequestParam Long spotId,
    		@AuthenticationPrincipal LoginUser loginUser) {

        Long userId = loginUser.getId();

        favoriteService.addFavorite(userId, spotId);

        return "redirect:/spot/" + spotId;
    }
}
