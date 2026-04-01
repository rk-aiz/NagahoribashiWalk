package com.example.nagahoribashi_walk.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
    
    @GetMapping("/mypage")
    public String mypage(
    		@AuthenticationPrincipal LoginUser loginUser,
    		Model model) {
    	
    	System.out.println(loginUser.getUsername());
    	System.out.println(loginUser.getId());
    	
    	return "user/mypage";
    }

}
