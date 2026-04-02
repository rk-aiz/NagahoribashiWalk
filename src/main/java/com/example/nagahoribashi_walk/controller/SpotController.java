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

import com.example.nagahoribashi_walk.dto.SpotDetail;
import com.example.nagahoribashi_walk.dto.SpotSummary;
import com.example.nagahoribashi_walk.service.FavoriteService;
import com.example.nagahoribashi_walk.service.SpotService;
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
    
    @GetMapping("/spot/category/all")
    public String list(
            @PageableDefault(size = 12) Pageable pageable, 
            Model model) {
        model.addAttribute("category", "all");
        model.addAttribute("spotPages", spotService.getPage(pageable));
        return "spot/list";
    } 
    
    @GetMapping("/spot/search")
    public String search(
    		@RequestParam("q") String keyword, 
    		@PageableDefault(size = 12) Pageable pageable, Model model) {

    	    Page<SpotSummary> page =
    	            spotService.searchByKeywords(keyword, pageable);

        model.addAttribute("page", page);
        model.addAttribute("keyword", keyword);

        return "spot/search";
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
    	}    	
    	
        SpotDetail spotDetail = spotService.findById(spotId, loginUserId);
        
        model.addAttribute("spotDetail", spotDetail);
        
        if (loginUser != null) {
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
