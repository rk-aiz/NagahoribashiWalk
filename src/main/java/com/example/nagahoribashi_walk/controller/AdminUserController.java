package com.example.nagahoribashi_walk.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagahoribashi_walk.dto.AdminUserRow;
import com.example.nagahoribashi_walk.service.UserService;
import com.example.nagahoribashi_walk.service.userdetails.LoginUser;

import lombok.RequiredArgsConstructor;

/**
 * ユーザー管理画面用のコントローラー
 *
 * @author 海津、篠原
 */
@Controller
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    /**
     * 【管理者】ユーザー一覧画面を表示
     */
    @GetMapping("/list")
    public String list(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "sort", defaultValue = "desc") String sort,
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(name = "includeDeleted", defaultValue = "false") boolean includeDeleted,
            Model model) {

        Page<AdminUserRow> page = userService.getAdminUserPage(pageable, sort, keyword, includeDeleted);

        if (page.isEmpty() && pageable.getPageNumber() > 0) {
            int lastPage = Math.max(0, page.getTotalPages() - 1);
            String redirect = "redirect:/admin/user/list?page=" + lastPage
                    + "&sort=" + sort + "&includeDeleted=" + includeDeleted;
            if (keyword != null)
                redirect += "&keyword=" + URLEncoder.encode(keyword, StandardCharsets.UTF_8);
            return redirect;
        }

        model.addAttribute("page", page);
        model.addAttribute("sort", sort);
        model.addAttribute("keyword", keyword);
        model.addAttribute("includeDeleted", includeDeleted);
        return "admin/user/list";
    }

    /**
     * 【管理者】ユーザーの有効 / 無効を切り替える
     */
    @PostMapping("/toggle")
    public String toggle(
            @RequestParam("id") Long id,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam("page") Integer page,
            @RequestParam("sort") String sort,
            @RequestParam(name = "includeDeleted", defaultValue = "false") boolean includeDeleted,
            RedirectAttributes redirectAttributes) {

        userService.toggleEnabled(id);

        redirectAttributes.addAttribute("page", page);
        redirectAttributes.addAttribute("sort", sort);
        redirectAttributes.addAttribute("includeDeleted", includeDeleted);
        if (keyword != null) {
            redirectAttributes.addAttribute("keyword", keyword);
        }

        return "redirect:/admin/user/list";
    }

    /**
     * 【管理者】ユーザーを削除(退会処理)する
     */
    @PostMapping("/delete")
    public String delete(
            @RequestParam("username") String username,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam("sort") String sort,
            @RequestParam("page") Integer page,
            @RequestParam(name = "includeDeleted", defaultValue = "false") boolean includeDeleted,
            @AuthenticationPrincipal LoginUser loginUser,
            RedirectAttributes redirectAttributes) {

        userService.delete(username, loginUser.getUsername());
        redirectAttributes.addFlashAttribute("message", username + "を削除しました。");

        redirectAttributes.addAttribute("page", page);
        redirectAttributes.addAttribute("sort", sort);
        redirectAttributes.addAttribute("includeDeleted", includeDeleted);
        if (keyword != null) {
            redirectAttributes.addAttribute("keyword", keyword);
        }

        return "redirect:/admin/user/list";
    }
}
