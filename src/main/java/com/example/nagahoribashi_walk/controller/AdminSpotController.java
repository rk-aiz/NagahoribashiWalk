package com.example.nagahoribashi_walk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.nagahoribashi_walk.form.SpotForm;
import com.example.nagahoribashi_walk.service.SpotService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminSpotController {

    private final SpotService spotService;

    /**
     * 管理者用スポット一覧画面
     */
    @GetMapping("/admin/spot/list")
    public String list() {

        return "admin/spot/list";
    }

    /**
     * 管理者用スポット新規登録画面
     */
    @GetMapping("/admin/spot/new")
    public String showNew(Model model) {
        SpotForm form = new SpotForm();
        form.setNew(true);
        model.addAttribute("form", form);
        return "admin/spot/edit";
    }

    /**
     * 管理者用スポット更新画面
     */
    @GetMapping("/admin/spot/edit")
    public String showEdit() {

        return "admin/spot/edit";
    }

    /**
     * スポット新規登録
     */
    @PostMapping("/admin/spot/new")
    public String register() {

        return "redirect:/admin/spot/list";
    }

    /**
     * スポット更新
     */
    @PostMapping("/admin/spot/edit")
    public String update() {

        return "redirect:/admin/spot/list";
    }
}
