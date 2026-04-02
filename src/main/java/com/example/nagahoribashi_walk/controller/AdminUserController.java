package com.example.nagahoribashi_walk.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.nagahoribashi_walk.dto.AdminUserRow;
import com.example.nagahoribashi_walk.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * 
 * @author 海津
 */
@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    public String userList(
            @PageableDefault(size = 12) Pageable pageable,
            Model model) {

        Page<AdminUserRow> page = userService.getAdminUserPage(pageable);

        model.addAttribute("page", page);

        return "admin/user/list";
    }

    @PostMapping("/toggle")
    public String toggle(@RequestParam Long id) {
        userService.toggleEnabled(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long id) {
        userService.delete(id);
        return "redirect:/admin/users";
    }
}
