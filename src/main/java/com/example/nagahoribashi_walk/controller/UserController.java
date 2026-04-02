package com.example.nagahoribashi_walk.controller;



import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.nagahoribashi_walk.service.UserService;

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
    		@AuthenticationPrincipal UserDetails userDetails,
    		Model model) {
    	
    	System.out.println(userDetails.getUsername());
    	
    	return "user/mypage";
    }
}
