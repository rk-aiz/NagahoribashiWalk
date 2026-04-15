package com.example.nagahoribashi_walk.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.nagahoribashi_walk.service.FortuneSlipService;
import com.example.nagahoribashi_walk.service.SpotService;
import com.example.nagahoribashi_walk.service.userdetails.LoginUser;

import lombok.RequiredArgsConstructor;

/**
 * ホーム(トップ画面)用のコントローラー
 *
 * @author 海津, 篠原
 */
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final SpotService spotService;
    private final FortuneSlipService fortuneSlipService;

    /**
     * トップページを表示する
     * SpotService から SpotSummary を3件取得して model に渡す
     */
    @GetMapping("/")
    public String showHome(@AuthenticationPrincipal LoginUser loginUser, Model model) {
        // SpotSummaryを3件取得してmodelにセット
        model.addAttribute("spots", spotService.getRecommendedSpots());

        // 次におみくじを引けるまでの秒数
        LocalDateTime nextDrawTime = Optional.ofNullable(loginUser)
                .map(u -> fortuneSlipService.getNextDrawTime(u))
                .orElse(LocalDateTime.MAX);

        //すでにおみくじを引いたかどうか（DBから最新値を参照）
        boolean drawnToday = loginUser != null && fortuneSlipService.isAlreadyDrawn(loginUser);

        model.addAttribute("drawnToday", drawnToday);
        model.addAttribute("loggedIn", loginUser != null);
        model.addAttribute("nextDrawTime", nextDrawTime);

        // home.htmlを表示
        return "home";
    }

}
