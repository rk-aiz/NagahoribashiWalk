package com.example.nagahoribashi_walk.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.nagahoribashi_walk.dto.SpotSummary;
import com.example.nagahoribashi_walk.service.SpotService;

import lombok.RequiredArgsConstructor;

/**
 * ホーム(トップ画面)用のコントローラー
 * 
 * @author 海津
 */
@Controller
@RequiredArgsConstructor
public class HomeController {

	private final SpotService spotService;

	@GetMapping("/")
	public String showHome(Model model) {

		//全スポット取得
		Page<SpotSummary> threeSpots = spotService.getPage(Pageable.ofSize(3));

		
		for(SpotSummary spot : threeSpots) {

			System.out.println(spot);
		}

		model.addAttribute("spots", threeSpots);
		return "home";
	}

}
