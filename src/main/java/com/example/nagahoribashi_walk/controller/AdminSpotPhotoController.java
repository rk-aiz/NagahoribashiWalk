package com.example.nagahoribashi_walk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.nagahoribashi_walk.service.SpotPhotoService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminSpotPhotoController {

    private final SpotPhotoService spotPhotoService;
    
    @GetMapping("/admin/spot/{id}/photo")
    public String showPhotoEdit(Model model) {
        return "/admin/spot/photo";
    }

}
