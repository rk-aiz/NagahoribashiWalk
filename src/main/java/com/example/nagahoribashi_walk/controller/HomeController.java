package com.example.nagahoribashi_walk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.nagahoribashi_walk.service.SpotService;

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

    /**
     * トップページを表示する
     * SpotService から SpotSummary を3件取得して model に渡す
     */
    @GetMapping("/")
    public String showHome(Model model) {
        // SpotSummaryを3件取得してmodelにセット
        model.addAttribute("spots", spotService.getRecommendedSpots());

        // home.htmlを表示
        return "home";
    }

}
