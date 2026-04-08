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
        // カラム: ID / スポット名 / 投稿者 / 評価 / コメント / 投稿日時 / 操作（削除ボタン）
        // 削除は POST /admin/review/delete/{id} + confirm() ダイアログ
        // コントローラー側で必要になる変数:

        // 変数 型 用途
        // reviewPages Page<?> テーブル + ページネーション
        // keyword String ページネーションのクエリ引き継ぎ（null でも可）
        // エンティティのフィールド名（spotName, userName, rating, comment,
        // createdAt）は実際のクラスに合わせて調整が必要です。

        model.addAttribute("reviewPages", reviewService.getAdminReviewPage(pageable, keyword));

        return "admin/review/list";
    }

    @PostMapping("/admin/review/delete")
    public String delete(
            @RequestParam("reviewId") Long reviewId,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam("page") Integer page,
            RedirectAttributes redirectAttributes
        ) {
            // レビューの削除処理
            reviewService.deleteForAdmin(reviewId);

            if (keyword != null) {
                redirectAttributes.addAttribute("keyword", keyword);
            }

            return "redirect:/admin/review/list?page=" + page;
    }

}
