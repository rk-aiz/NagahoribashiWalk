package com.example.nagahoribashi_walk.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.nagahoribashi_walk.dto.AdminUserRow;
import com.example.nagahoribashi_walk.service.UserService;
import com.example.nagahoribashi_walk.service.userdetails.LoginUser;

import lombok.RequiredArgsConstructor;

/**
 * 
 * @author 海津
 */
@Controller
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @GetMapping("/list")
    public String userList(
    		@RequestParam(defaultValue = "desc")String sort,
            @PageableDefault(size = 5) Pageable pageable,
            Model model) {

        Page<AdminUserRow> page = userService.getAdminUserPage(pageable,sort);

        model.addAttribute("page", page);
        model.addAttribute("sort",sort);

        return "admin/user/list";
    }

    @PostMapping("/toggle")
    public String toggle(@RequestParam Long id) {
        userService.toggleEnabled(id);
        return "redirect:/admin/user/list";
    }

    @PostMapping("/delete")
    public String delete(
    		@RequestParam("username") String username, 
    		@AuthenticationPrincipal LoginUser loginUser) {

        userService.delete(username, loginUser.getUsername());

        return "redirect:/admin/user/list";
    }
}
