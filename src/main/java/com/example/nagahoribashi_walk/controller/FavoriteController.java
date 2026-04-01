package com.example.nagahoribashi_walk.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.nagahoribashi_walk.service.FavoriteService;

@Controller
//@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;
    
    @PostMapping("/favorite/remove")
    public String favorite(@RequestParam Long spotId,
                           @AuthenticationPrincipal UserDetails userDetails) {

        String userId = userDetails.getUsername();

        favoriteService.removeFavorite(userId, spotId);

        return "redirect:/spot/" + spotId;
    }
}
