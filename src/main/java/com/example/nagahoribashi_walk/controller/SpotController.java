package com.example.nagahoribashi_walk.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import com.example.nagahoribashi_walk.dto.NavCategory;
import com.example.nagahoribashi_walk.dto.SidebarDTO;
import com.example.nagahoribashi_walk.dto.SpotDetail;
import com.example.nagahoribashi_walk.dto.SpotSummary;
import com.example.nagahoribashi_walk.service.CategoryService;
import com.example.nagahoribashi_walk.service.FavoriteService;
import com.example.nagahoribashi_walk.service.SpotService;
import com.example.nagahoribashi_walk.service.SubCategoryService;
import com.example.nagahoribashi_walk.service.userdetails.LoginUser;

import lombok.RequiredArgsConstructor;

/**
 * @author 海津
 */
@Controller
@RequiredArgsConstructor
public class SpotController {

    private final SpotService spotService;
    private final FavoriteService favoriteService;
    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;

    @GetMapping("/spot/category/all")
    public String list(
            @PageableDefault(size = 12) Pageable pageable,
            Model model) {
        model.addAttribute("categoryId", "all");
        model.addAttribute("categoryName", "すべて");
        model.addAttribute("spotPages", spotService.getPage(pageable));
        model.addAttribute("sidebar", new SidebarDTO(categoryService.findAll(), null, null));
        return "spot/list";
    }

    @GetMapping("/spot/search")
    public String search(
            @RequestParam("q") String keyword,
            @PageableDefault(size = 12) Pageable pageable, Model model) {
        Page<SpotSummary> page = spotService.searchByKeywords(keyword, pageable);
        model.addAttribute("page", page);
        model.addAttribute("keyword", keyword);

        return "spot/search";
    }

    // 大谷記載
    @GetMapping("/spot/category/{categoryId}")
    public String listByCategoryId(
            @PathVariable("categoryId") Long categoryId,
            @PageableDefault(size = 12) Pageable pageable, Model model) {

        model.addAttribute("spotPages", spotService.getPageByCategoryId(categoryId, pageable));
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("categoryName", categoryService.getById(categoryId).getName());
        model.addAttribute("sidebar", new SidebarDTO(categoryService.findAll(), categoryId, null));

        return "spot/list";
    }

    @GetMapping("/spot/subcategory/{subCategoryId}")
    public String listBySubCategoryId(
            @PathVariable("subCategoryId") Long subCategoryId,
            @PageableDefault(size = 12) Pageable pageable, Model model) {

        List<NavCategory> navCategories = categoryService.findAll();
        Long parentCategoryId = navCategories.stream()
                .filter(cat -> cat.getSubCategories().stream()
                        .anyMatch(sub -> sub.getId().equals(subCategoryId)))
                .map(NavCategory::getId)
                .findFirst()
                .orElse(null);

        model.addAttribute("spotPages", spotService.getPageBySubCategoryId(subCategoryId, pageable));
        model.addAttribute("categoryId", subCategoryId);
        model.addAttribute("categoryName", subCategoryService.getById(subCategoryId).getName());
        model.addAttribute("sidebar", new SidebarDTO(navCategories, parentCategoryId, subCategoryId));

        return "spot/list";
    }

    /**
     * スポット詳細画面表示
     */
    @GetMapping("/spot/{spotId}")
    public String detail(
            @AuthenticationPrincipal LoginUser loginUser,
            @PathVariable("spotId") Long spotId, Model model) {

        Long loginUserId = null;

        if (loginUser != null) {
            loginUserId = loginUser.getId();
            model.addAttribute("isFavorite",
                    favoriteService.isFavorite(loginUserId, spotId));
        } else {
            // null参照を避けるためにfalseをセット
            model.addAttribute("isFavorite", false);
        }

        model.addAttribute("spotDetail",
                spotService.findById(spotId, loginUserId));

        return "spot/detail";
    }
}
