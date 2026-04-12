package com.example.nagahoribashi_walk.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagahoribashi_walk.service.FavoriteService;
import com.example.nagahoribashi_walk.service.FortuneSlipService;
import com.example.nagahoribashi_walk.service.UserService;
import com.example.nagahoribashi_walk.service.userdetails.LoginUser;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;

/**
 * おみくじ
 *
 * @author 海津
 */
@Controller
@RequiredArgsConstructor
public class FortuneSlipController {

    @Value("${fortune.bonus-point}")
    private int fortuneBonusPoint;

    private final FortuneSlipService fortuneSlipService;
    private final FavoriteService favoriteService;
    private final UserService userService;

    /** 気分選択ページ */
    @GetMapping("/fortune")
    public String showFortuneSlip(
            @AuthenticationPrincipal LoginUser loginUser,
            Model model) {

        // テスト中は何度も引ける TODO : 本番ではコメントアウト解除
        // if (fortuneSlipService.isAlreadyDrawn(loginUser)) {
        // return "redirect:/fortune-result";
        // }

        if (loginUser != null) {
            model.addAttribute("moodSelection",
                    fortuneSlipService.getMoodSelection(loginUser));
            model.addAttribute("profile",
                    userService.getProfileByUsername(loginUser.getUsername()));
        }

        return "/user/fortune-slip";
    }

    /** おみくじを引く */
    @PostMapping("/fortune/draw")
    public String draw(
            @RequestParam(name = "themeId", required = false) Long themeId,
            @AuthenticationPrincipal LoginUser loginUser) {

        fortuneSlipService.draw(themeId, loginUser);

        return "redirect:/fortune/result";
    }

    /** おみくじ結果画面からお気に入り登録（おすすめスポットならボーナスポイント付与） */
    @PostMapping("/fortune/favorite/add")
    public String addFavorite(
            @RequestParam("spotId") Long spotId,
            @AuthenticationPrincipal LoginUser loginUser,
            RedirectAttributes redirectAttributes) {

        int pointDelta = favoriteService.addFavorite(loginUser.getId(), spotId);
        if (pointDelta > 0) {
            redirectAttributes.addFlashAttribute("message", "お気に入りに追加しました。+" + pointDelta + " pt ゲット！");
        }
        return "redirect:/fortune/result";
    }

    /** 結果ページ */
    @GetMapping("/fortune/result")
    public String showFortuneResult(
            @AuthenticationPrincipal LoginUser loginUser,
            Model model) {

        if (!fortuneSlipService.isAlreadyDrawn(loginUser)) {
            return "redirect:/fortune";
        }
        model.addAttribute("fortuneResult",
                fortuneSlipService.getFortuneResult(loginUser));
        model.addAttribute("profile",
                userService.getProfileByUsername(loginUser.getUsername()));
        model.addAttribute("fortuneBonusPoint", fortuneBonusPoint);

        return "/user/fortune-result";
    }

}
