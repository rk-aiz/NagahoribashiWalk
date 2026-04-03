package com.example.nagahoribashi_walk.controller;



import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.nagahoribashi_walk.service.FavoriteService;
import com.example.nagahoribashi_walk.service.UserService;
import com.example.nagahoribashi_walk.service.userdetails.LoginUser;

import lombok.RequiredArgsConstructor;

/**
 * @author 海津
 */
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final FavoriteService favoriteService;

    @GetMapping("/mypage")
    public String mypage(
    		@AuthenticationPrincipal LoginUser loginUser,
    		@RequestParam(defaultValue = "profile") String tab,
    		@PageableDefault(size = 12) Pageable pageable,
    		Model model) {

    	model.addAttribute("profile",
    			userService.getProfileByUsername(loginUser.getUsername()));

    	model.addAttribute("favorites",
    			favoriteService.getPage(loginUser.getId(), pageable));

    	model.addAttribute("activeTab", tab);

    	return "/user/mypage";
    }
}
