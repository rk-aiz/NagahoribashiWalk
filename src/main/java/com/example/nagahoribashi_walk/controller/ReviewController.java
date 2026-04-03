package com.example.nagahoribashi_walk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagahoribashi_walk.entity.Review;
import com.example.nagahoribashi_walk.service.ReviewService;

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
    
    @PostMapping("/spot/{id}/reviews")
    public String addReview(
    		@PathVariable Long id,Review review,
    		RedirectAttributes redirectAttributes) {
    	//スポットidをセット
    	review.setSpotId(id);
    	
    	try {
        	//Service呼び出し
    		reviewService.addReview(review); // ←失敗したらthrow
    	} catch (Exception e) {
    		redirectAttributes.addFlashAttribute("errorMassage", e.getLocalizedMessage());
    	} 
    	
    	//投稿後は詳細画面にリダイレクト
    	return "redirect:/spot/" + id;
    }

}
