package com.example.nagahoribashi_walk.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.nagahoribashi_walk.dto.SpotSummary;
import com.example.nagahoribashi_walk.service.SpotService;

import lombok.RequiredArgsConstructor;

/**
 * @author 海津
 */
@Controller
@RequiredArgsConstructor
public class SpotController {

    private final SpotService spotService;
    
    @GetMapping("/spot/category/all")
    public String list(@PageableDefault(size = 12) Pageable pageable, Model model) {
    	
    	model.addAttribute("spots", spotService.getPage(pageable));
    	
        return "spot/list";
    } 
    
    @GetMapping("/spot")
    public String search(String keyword, Pageable pageable, Model model) {

        Page<SpotSummary> page =
                spotService.searchByKeywords(keyword, pageable);

        model.addAttribute("page", page);
        model.addAttribute("keyword", keyword);

        return "spot/list";
    }
}
