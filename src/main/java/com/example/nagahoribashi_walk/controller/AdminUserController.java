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
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "sort", defaultValue = "desc") String sort,
            @PageableDefault(size = 5) Pageable pageable,
            Model model) {

        Page<AdminUserRow> page = userService.getAdminUserPage(pageable, sort, keyword);

        model.addAttribute("page", page);
        model.addAttribute("sort", sort);
        model.addAttribute("keyword", keyword);
        return "admin/user/list";
    }

    @PostMapping("/toggle")
    public String toggle(@RequestParam Long id,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam("page") Integer page,
            @RequestParam("sort") String sort) {

        // ユーザーの有効無効切り替え処理
        userService.toggleEnabled(id);

        return "redirect:/admin/user/list?page=" + page + "&sort=" + sort + "&keyword=" + keyword;
    }

    @PostMapping("/delete")
    public String delete(
            @RequestParam("username") String username,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam("sort") String sort,
            @RequestParam("page") Integer page,
            @AuthenticationPrincipal LoginUser loginUser) {

        // ユーザーの(論理)削除処理
        userService.delete(username, loginUser.getUsername());

        return "redirect:/admin/user/list?page=" + page + "&sort=" + sort + "&keyword=" + keyword;
    }
}
