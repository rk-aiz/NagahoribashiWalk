package com.example.nagahoribashi_walk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagahoribashi_walk.service.SpotPhotoService;
import com.example.nagahoribashi_walk.service.SpotService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminSpotPhotoController {

    private final SpotService spotService;
    private final SpotPhotoService spotPhotoService;
    
    @GetMapping("/admin/spot/{spotId}/photo")
    public String showPhotoEdit(
            @PathVariable("spotId") Long spotId,
            Model model) {

        model.addAttribute("spot", spotService.getByIdForAdmin(spotId));
        model.addAttribute("photos", spotPhotoService.getAllBySpotId(spotId));

        return "/admin/spot/photo";
    }

    @PostMapping("/admin/spot/photo/delete")
    public String delete(
            @RequestParam("photoId") Long photoId,
            @RequestParam("spotId") Long spotId,
            RedirectAttributes redirectAttributes,
            Model model) {
        
        spotPhotoService.delete(photoId);
        redirectAttributes.addAttribute("message", "画像を削除しました");

        return "redirect:/admin/spot/" + spotId + "/photo";
    }

    @PostMapping("/admin/spot/{spotId}/photo/upload")
    public String upload(
            @PathVariable("spotId") Long spotId,
            RedirectAttributes redirectAttributes,
            Model model) {

        return "redirect:/admin/spot/" + spotId + "/photo";
    }

}
