package com.example.nagahoribashi_walk.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagahoribashi_walk.service.FavoriteService;
import com.example.nagahoribashi_walk.service.UserService;
import com.example.nagahoribashi_walk.service.userdetails.LoginUser;

import lombok.RequiredArgsConstructor;

/**
 * お気に入り登録解除用のコントローラー
 *
 * @author 正本, 海津
 */
@Controller
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final UserService userService;

    /**
     * スポット詳細ページからお気に入り登録
     */
    @PostMapping("/favorite/add/{spotId}")
    public String add(
            @PathVariable("spotId") Long spotId,
            @AuthenticationPrincipal LoginUser loginUser) {

        favoriteService.addFavorite(loginUser.getId(), spotId);

        return "redirect:/spot/" + spotId;
    }

    /**
     * スポット詳細ページからお気に入り解除
     */
    @PostMapping("/favorite/remove/{spotId}")
    public String remove(
            @PathVariable("spotId") Long spotId,
            @AuthenticationPrincipal LoginUser loginUser) {

        favoriteService.removeFavorite(loginUser.getId(), spotId);

        return "redirect:/spot/" + spotId;
    }

    /**
     * マイページから、お気に入り登録解除
     */
    @PostMapping("/mypage/favorite/remove/{spotId}")
    public String removeFromMypage(
            @PathVariable("spotId") Long spotId,
            @AuthenticationPrincipal LoginUser loginUser,
            @PageableDefault(size = 12) Pageable pageable,
            RedirectAttributes redirectAttributes,
            Model model) {

        favoriteService.removeFavorite(loginUser.getId(), spotId);

        redirectAttributes.addAttribute("tab", "favorites");
        redirectAttributes.addAttribute("edit", false);

        return "redirect:/mypage";
    }

}
