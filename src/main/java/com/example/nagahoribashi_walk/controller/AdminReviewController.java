package com.example.nagahoribashi_walk.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagahoribashi_walk.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminReviewController {

    private final ReviewService reviewService;

    @GetMapping("/admin/review/list")
    public String list(
            @RequestParam(name = "keyword", required = false) String keyword,
            @PageableDefault(size = 10) Pageable pageable,
            Model model) {

        model.addAttribute("reviewPages", reviewService.getAdminReviewPage(pageable, keyword));

        return "admin/review/list";
    }

    @PostMapping("/admin/review/delete")
    public String delete(
            @RequestParam("reviewId") Long reviewId,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam("page") Integer page,
            RedirectAttributes redirectAttributes) {
        // レビューの削除処理
        reviewService.deleteForAdmin(reviewId);

        if (keyword != null) {
            redirectAttributes.addAttribute("keyword", keyword);
        }
        redirectAttributes.addFlashAttribute("message",
                String.format("レビューID %d を削除しました", reviewId));

        return "redirect:/admin/review/list?page=" + page;
    }

}
