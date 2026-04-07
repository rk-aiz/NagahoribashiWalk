package com.example.nagahoribashi_walk.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
     * レビュー編集画面を表示する
     * 
     * @param reviewId 編集対象のレビューID
     * @param loginUser ログインユーザー
     * @param model モデル
     * @param redirectAttributes リダイレクト先へ渡すメッセージ
     * @return レビュー編集画面、またはスポット詳細画面へのリダイレクト
     */
    @GetMapping("/reviews/{id}/edit")
    public String editReviewForm(
            @PathVariable("id") Long reviewId,
            @AuthenticationPrincipal LoginUser loginUser,
            Model model,
            RedirectAttributes redirectAttributes) {

        // 未ログインの場合は編集させない
        if (loginUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "レビューを編集するにはログインが必要です。");
            return "redirect:/";
        }

        // 編集対象のレビューを取得
        Review review = reviewService.findById(reviewId);

        // 自分のレビュー以外は編集させない
        if (!review.getUserId().equals(loginUser.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "他のユーザーのレビューは編集できません。");
            return "redirect:/spot/" + review.getSpotId();
        }

        // 画面表示用にレビュー情報を渡す
        model.addAttribute("review", review);

        // レビュー編集画面を表示
        return "review/edit";
    }

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
    
    /**
     * レビュー更新処理
     * 
     * @param reviewId 編集対象のレビューID
     * @param reviewForm フォーム入力値
     * @param loginUser ログインユーザー
     * @param redirectAttributes リダイレクト先へ渡すメッセージ
     * @return スポット詳細画面へのリダイレクト
     */
    @PostMapping("/reviews/{id}/update")
    public String updateReview(
            @PathVariable("id") Long reviewId,
            ReviewForm reviewForm,
            @AuthenticationPrincipal LoginUser loginUser,
            RedirectAttributes redirectAttributes) {

        // 未ログインの場合は更新させない
        if (loginUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "レビューを更新するにはログインが必要です。");
            return "redirect:/";
        }

        // 更新対象のレビュー情報を生成
        Review review = new Review();

        // 更新対象のレビューIDをセット
        review.setId(reviewId);

        // フォーム入力値をセット
        review.setRating(reviewForm.getRating());
        review.setComment(reviewForm.getComment());

        try {
            // 更新処理を実行
            reviewService.updateReview(review, loginUser.getId());

            // 更新後のメッセージ
            redirectAttributes.addFlashAttribute("message", "レビューを更新しました。");

        } catch (IllegalArgumentException e) {
            // エラーメッセージを設定
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        // 元レビューを取得して、スポット詳細へ戻す
        Review existingReview = reviewService.findById(reviewId);
        return "redirect:/spot/" + existingReview.getSpotId() + "#review-" + reviewId;
    }
    
    /**
     * レビュー削除処理
     * 
     * @param reviewId 削除対象のレビューID
     * @param loginUser ログインユーザー
     * @param redirectAttributes リダイレクト先へ渡すメッセージ
     * @return スポット詳細画面へのリダイレクト
     */
    @PostMapping("/reviews/{id}/delete")
    public String deleteReview(
            @PathVariable("id") Long reviewId,
            @AuthenticationPrincipal LoginUser loginUser,
            RedirectAttributes redirectAttributes) {

        // 未ログインの場合は削除させない
        if (loginUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "レビューを削除するにはログインが必要です。");
            return "redirect:/";
        }

        // 削除後はレビュー自体が消えるので、先に spotId を取得しておく
        Review existingReview = reviewService.findById(reviewId);

        if (existingReview == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "対象のレビューが存在しません。");
            return "redirect:/";
        }

        try {
            // 削除処理を実行
            reviewService.deleteReview(reviewId, loginUser.getId());

            // 成功メッセージを設定
            redirectAttributes.addFlashAttribute("message", "レビューを削除しました。");

        } catch (IllegalArgumentException e) {
            // エラーメッセージを設定
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        // 元のスポット詳細画面へ戻す
        return "redirect:/spot/" + existingReview.getSpotId();
    }

}
