package com.example.nagahoribashi_walk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminHomeController {

	@GetMapping("/admin/home")
	public String home() {
		return "admin/admin_home";
	}
	
}
