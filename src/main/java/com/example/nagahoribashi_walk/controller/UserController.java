package com.example.nagahoribashi_walk.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
            @RequestParam(name = "tab", defaultValue = "profile") String tab,
            @PageableDefault(size = 12) Pageable pageable,
            Model model) {

        model.addAttribute("profile",
                userService.getProfileByUsername(loginUser.getUsername()));

        model.addAttribute("favorites",
                favoriteService.getPage(loginUser.getId(), pageable));

        model.addAttribute("activeTab", tab);

        return "/user/mypage";
    }
    
    //ユーザー退会用
    @PostMapping("/unsubscribe")
    //Authentication(誰をログアウトするか)
    //Request(セッション情報を消す)
    //Response(Cookieを消す)
    public String unsubscribe(HttpServletRequest request,
                              HttpServletResponse response,
                              Authentication authentication) {

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        Long userId = loginUser.getId();

        userService.unsubscribe(userId);

        new SecurityContextLogoutHandler().logout(request, response, authentication);

        return "redirect:/";
    }
}
