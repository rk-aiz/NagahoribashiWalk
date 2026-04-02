package com.example.nagahoribashi_walk.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.nagahoribashi_walk.service.FavoriteService;
import com.example.nagahoribashi_walk.service.userdetails.LoginUser;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService ;
    
    @PostMapping("/favorite/add/{spotId}")
    public String add(
    		@PathVariable("spotId") Long spotId,
    		@AuthenticationPrincipal LoginUser loginUser) {

        favoriteService.addFavorite(loginUser.getId(), spotId);

        return "redirect:/spot/" + spotId;
    }
    
    @PostMapping("/favorite/delete/{spotId}")
    public String delete(
    		@PathVariable("spotId") Long spotId,
    		@AuthenticationPrincipal LoginUser loginUser) {
    	
        favoriteService.removeFavorite(loginUser.getId(), spotId);

        return "redirect:/spot/" + spotId;
    }
    
}
