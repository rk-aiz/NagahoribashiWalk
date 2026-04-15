package com.example.nagahoribashi_walk.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagahoribashi_walk.dto.AdminReviewRow;
import com.example.nagahoribashi_walk.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminReviewController {

    private final ReviewService reviewService;

    /**
     * 【管理者】レビュー一覧を表示する
     */
    @GetMapping("/admin/review/list")
    public String list(
            @RequestParam(name = "keyword", required = false) String keyword,
            @PageableDefault(size = 10) Pageable pageable,
            Model model) {

        Page<AdminReviewRow> reviewPages = reviewService.getAdminReviewPage(pageable, keyword);

        if (reviewPages.isEmpty() && pageable.getPageNumber() > 0) {
            int lastPage = Math.max(0, reviewPages.getTotalPages() - 1);
            String redirect = "redirect:/admin/review/list?page=" + lastPage;
            if (keyword != null)
                redirect += "&keyword=" + URLEncoder.encode(keyword, StandardCharsets.UTF_8);
            return redirect;
        }

        model.addAttribute("reviewPages", reviewPages);
        model.addAttribute("keyword", keyword);

        return "admin/review/list";
    }

    /**
     * 【管理者】レビューを削除する
     */
    @PostMapping("/admin/review/delete")
    public String delete(
            @RequestParam("reviewId") Long reviewId,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam("page") Integer page,
            RedirectAttributes redirectAttributes) {
        // レビューの削除処理
        reviewService.deleteForAdmin(reviewId);

        redirectAttributes.addFlashAttribute("message",
                String.format("レビューID %d を削除しました", reviewId));
        redirectAttributes.addAttribute("page", page);
        if (keyword != null) {
            redirectAttributes.addAttribute("keyword", keyword);
        }

        return "redirect:/admin/review/list";
    }

    /**
     * 【管理者】スポット詳細画面からレビューを削除する
     */
    @PostMapping("/admin/review/delete/form-spot")
    public String deleteFormSpot(
            @RequestParam("reviewId") Long reviewId,
            @RequestParam("spotId") Long spotId,
            RedirectAttributes redirectAttributes) {

        // 管理者用レビュー削除処理
        reviewService.deleteForAdmin(reviewId);

        // 完了メッセージ
        redirectAttributes.addFlashAttribute("message",
                String.format("レビューID %d を削除しました", reviewId));

        // 元のスポット詳細画面へ戻す
        return "redirect:/spot/" + spotId;

    }

}
