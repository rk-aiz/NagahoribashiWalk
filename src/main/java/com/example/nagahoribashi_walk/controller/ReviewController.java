package com.example.nagahoribashi_walk.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagahoribashi_walk.entity.Review;
import com.example.nagahoribashi_walk.exception.ReviewAlreadyExistsException;
import com.example.nagahoribashi_walk.form.ReviewForm;
import com.example.nagahoribashi_walk.service.ReviewService;
import com.example.nagahoribashi_walk.service.userdetails.LoginUser;

import lombok.RequiredArgsConstructor;

/**
 * レビュー関連コントローラー
 */

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * レビュー投稿処理
     */

    @PostMapping("/review/post/{spotId}")
    public String addReview(
            ReviewForm reviewForm,
            @PathVariable("spotId") Long spotId,
            @AuthenticationPrincipal LoginUser loginUser,
            RedirectAttributes redirectAttributes) {

        // 未ログインの場合は投稿させない
        if (loginUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "レビューを投稿するにはログインが必要です。");
            return "redirect:/spot/" + spotId;
        }

        // レビュー情報を生成する
        Review review = new Review();

        // スポットidをセット
        review.setSpotId(spotId);

        // フォームの入力値をセット
        review.setRating(reviewForm.getRating());
        review.setComment(reviewForm.getComment());

        try {
            // Service呼び出し
            reviewService.addReview(review, loginUser.getId()); // ←失敗したらthrow

            // 成功メッセージを設定する
            redirectAttributes.addFlashAttribute("message", "レビューを投稿しました。");
        } catch (ReviewAlreadyExistsException e) {
            // エラーメッセージを設定する
            redirectAttributes.addFlashAttribute("errorMessage", e.getLocalizedMessage());
        }

        // 投稿後は詳細画面にリダイレクト
        return "redirect:/spot/" + spotId;
    }

}
