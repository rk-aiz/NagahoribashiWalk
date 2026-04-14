package com.example.nagahoribashi_walk.controller;

import java.util.Locale;

import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagahoribashi_walk.entity.Review;
import com.example.nagahoribashi_walk.form.ReviewForm;
import com.example.nagahoribashi_walk.service.FavoriteService;
import com.example.nagahoribashi_walk.service.ReviewService;
import com.example.nagahoribashi_walk.service.SpotService;
import com.example.nagahoribashi_walk.service.userdetails.LoginUser;

import lombok.RequiredArgsConstructor;

/**
 * レビュー関連コントローラー
 */
@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final SpotService spotService;
    private final FavoriteService favoriteService;
    private MessageSource messageSource;

    /**
     * レビュー投稿処理
     */
    @PostMapping("/review/post/{spotId}")
    public String addReview(
            @Validated ReviewForm form,
            BindingResult bindingResult,
            @PathVariable("spotId") Long spotId,
            @AuthenticationPrincipal LoginUser loginUser,
            RedirectAttributes redirectAttributes,
            Locale locale,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage",
                    bindingResult.getAllErrors().getFirst().getDefaultMessage());
            model.addAttribute("isFavorite",
                    favoriteService.isFavorite(loginUser.getId(), spotId));
            model.addAttribute("spotDetail",
                    spotService.getById(spotId, loginUser.getId()));
            return "/spot/detail";
        }

        Review review = new Review();
        BeanUtils.copyProperties(form, review);
        review.setSpotId(spotId);

        int pointDelta = reviewService.addReview(review, loginUser.getId());
        redirectAttributes.addFlashAttribute("message", "レビューを投稿しました。+" + pointDelta + " pt ゲット！");

        return "redirect:/spot/" + spotId;
    }

    /**
     * レビュー更新処理
     */
    @PostMapping("/review/{id}/update")
    public String updateReview(
            @PathVariable("id") Long reviewId,
            @Validated ReviewForm form,
            BindingResult bindingResult,
            @AuthenticationPrincipal LoginUser loginUser,
            RedirectAttributes redirectAttributes,
            Model model) {

        Review existing = reviewService.getById(reviewId);

        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage",
                    bindingResult.getAllErrors().getFirst().getDefaultMessage());
            model.addAttribute("isFavorite",
                    favoriteService.isFavorite(loginUser.getId(), existing.getSpotId()));
            model.addAttribute("spotDetail",
                    spotService.getById(existing.getSpotId(), loginUser.getId()));
            model.addAttribute("editReviewId", reviewId);
            return "/spot/detail";
        }

        existing.setRating(form.getRating());
        existing.setComment(form.getComment());

        reviewService.updateReview(existing, loginUser.getId());
        redirectAttributes.addFlashAttribute("message", "レビューを更新しました。");

        return "redirect:/spot/" + existing.getSpotId() + "#review-" + reviewId;
    }

    /**
     * レビュー削除処理
     */
    @PostMapping("/review/{id}/delete")
    public String deleteReview(
            @PathVariable("id") Long reviewId,
            @AuthenticationPrincipal LoginUser loginUser,
            RedirectAttributes redirectAttributes) {

        Review existing = reviewService.getById(reviewId);

        int pointDelta = reviewService.deleteReview(reviewId, loginUser.getId());
        if (pointDelta < 0) {
            redirectAttributes.addFlashAttribute("message", "レビューを削除しました。" + pointDelta + " pt");
        } else {
            redirectAttributes.addFlashAttribute("message", "レビューを削除しました。");
        }

        return "redirect:/spot/" + existing.getSpotId();
    }
}
