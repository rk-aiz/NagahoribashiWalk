package com.example.nagahoribashi_walk.controller;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.nagahoribashi_walk.dto.ReviewDTO;
import com.example.nagahoribashi_walk.dto.SpotDetail;
import com.example.nagahoribashi_walk.dto.SpotSummary;
import com.example.nagahoribashi_walk.form.ReviewForm;
import com.example.nagahoribashi_walk.service.CategoryService;
import com.example.nagahoribashi_walk.service.FavoriteService;
import com.example.nagahoribashi_walk.service.SpotService;
import com.example.nagahoribashi_walk.service.SubCategoryService;
import com.example.nagahoribashi_walk.service.userdetails.LoginUser;

import lombok.RequiredArgsConstructor;

/**
 * スポット関連のコントローラー
 * 
 * @author 海津
 */
@Controller
@RequiredArgsConstructor
public class SpotController {

    private final SpotService spotService;
    private final FavoriteService favoriteService;
    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;

    /**
     * 全てのスポットを一覧表示する
     * 
     * @param pageable ページネーション情報
     * @param model    モデル
     * @return スポット一覧画面のパス
     */
    @GetMapping("/spot/category/all")
    public String list(
            @PageableDefault(size = 12) Pageable pageable,
            Model model) {
        model.addAttribute("categoryId", "all");
        model.addAttribute("categoryName", "すべて");
        model.addAttribute("spotPages", spotService.getPage(pageable));
        model.addAttribute("pagerBaseUrl", "/spot/category/");
        // サイドバー用の共通データを取得（カテゴリ一覧など）
        model.addAttribute("sidebar", categoryService.getSidebarDTO(null));
        return "spot/list";
    }

    /**
     * キーワードによるスポット検索結果を表示する
     * 
     * @param keyword  検索キーワード
     * @param pageable ページネーション情報
     * @param model    モデル
     * @return スポット検索結果画面のパス
     */
    @GetMapping("/spot/search")
    public String search(
            @RequestParam("q") String keyword,
            @PageableDefault(size = 12) Pageable pageable, Model model) {
        Page<SpotSummary> page = spotService.searchByKeywords(keyword, pageable);
        model.addAttribute("page", page);
        model.addAttribute("keyword", keyword);

        return "spot/search";
    }

    /**
     * 指定されたカテゴリに属するスポットを一覧表示する
     * 
     * @param categoryId カテゴリID
     * @param pageable   ページネーション情報
     * @param model      モデル
     * @return スポット一覧画面のパス
     */
    @GetMapping("/spot/category/{categoryId}")
    public String listByCategoryId(
            @PathVariable("categoryId") Long categoryId,
            @PageableDefault(size = 12) Pageable pageable, Model model) {

        model.addAttribute("sidebar",
                categoryService.getSidebarDTO(categoryId));

        model.addAttribute("spotPages", spotService.getPageByCategoryId(categoryId, pageable));
        model.addAttribute("pagerBaseUrl", "/spot/category/");

        model.addAttribute("categoryId", categoryId);
        model.addAttribute("categoryName", categoryService.getById(categoryId).getName());

        return "spot/list";
    }

    /**
     * 指定されたサブカテゴリに属するスポットを一覧表示する
     * 
     * @param subCategoryId サブカテゴリID
     * @param pageable      ページネーション情報
     * @param model         モデル
     * @return スポット一覧画面のパス
     */
    @GetMapping("/spot/subcategory/{subCategoryId}")
    public String listBySubCategoryId(
            @PathVariable("subCategoryId") Long subCategoryId,
            @PageableDefault(size = 12) Pageable pageable, Model model) {

        model.addAttribute("sidebar",
                subCategoryService.getSidebarDTO(subCategoryId));

        model.addAttribute("spotPages", spotService.getPageBySubCategoryId(subCategoryId, pageable));
        model.addAttribute("pagerBaseUrl", "/spot/subcategory/");

        model.addAttribute("categoryId", subCategoryId);
        model.addAttribute("categoryName", subCategoryService.getById(subCategoryId).getName());

        return "spot/list";
    }

    /**
     * スポット詳細画面を表示する
     * 
     * @param loginUser    ログインユーザー情報（未ログイン時はnull）
     * @param spotId       スポットID
     * @param editReviewId 編集中のレビューID
     * @param model        モデル
     * @return スポット詳細画面のパス
     */
    @GetMapping("/spot/{spotId}")
    public String detail(
            @AuthenticationPrincipal LoginUser loginUser,
            @PathVariable("spotId") Long spotId,
            @RequestParam(name = "editReviewId", required = false) Long editReviewId,
            Model model) {

        Long loginUserId = null;

        // ログイン状態に応じてお気に入り登録状況を確認
        if (loginUser != null) {
            loginUserId = loginUser.getId();
            model.addAttribute("isFavorite",
                    favoriteService.isFavorite(loginUserId, spotId));
        } else {
            // 未ログイン時は常にお気に入り未登録として扱う
            // null参照を避けるためにfalseをセット
            model.addAttribute("isFavorite", false);
        }

        SpotDetail spotDetail = spotService.findById(spotId, loginUserId);
        // スポット詳細情報を画面へ渡す
        model.addAttribute("spotDetail", spotDetail);

        // どのレビューを編集中かを画面へ渡す
        model.addAttribute("editReviewId", editReviewId);
        
        ReviewForm reviewForm = new ReviewForm();
        spotDetail.getReviews().stream()
                .filter(r -> r.isMyReview())
                .findFirst()
                .ifPresent(r -> {
                    BeanUtils.copyProperties(r, reviewForm);
                });
        model.addAttribute("reviewForm", reviewForm);
        
        return "spot/detail";
    }
}
