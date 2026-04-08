package com.example.nagahoribashi_walk.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
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
    public String showSpotPhoto(
            @PathVariable("spotId") Long spotId,
            Model model) {

        model.addAttribute("spot", spotService.getByIdForAdmin(spotId));
        model.addAttribute("photos", spotPhotoService.getAllBySpotId(spotId));

        return "/admin/spot/photo";
    }

    @PostMapping("/admin/spot/{spotId}/photo/upload")
    public String uploadImages(
            @PathVariable("spotId") Long spotId,
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("displayOrder") Integer displayOrder,
            RedirectAttributes redirectAttributes) {

        List<String> savedFilenames = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        for (MultipartFile file : files) {

            if (file.isEmpty() || (!Optional.ofNullable(file.getContentType()).orElse("").startsWith("image/"))) {
                errors.add(file.getOriginalFilename() + ": スキップ");
                continue;
            }
            try {
                String filename = spotPhotoService.saveImage(file);
                savedFilenames.add(filename);
            } catch (IOException e) {
                errors.add(file.getOriginalFilename() + ": 保存失敗");
            }
        }

        if (!savedFilenames.isEmpty()) {
            redirectAttributes.addFlashAttribute("message",
                    savedFilenames.size() + "件アップロードしました");
        }
        if (!errors.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    String.join(", ", errors));
        }

        return "redirect:/admin/spot/" + spotId + "/photo";
    }

    @PostMapping("/admin/spot/photo/delete")
    public String delete(
            @RequestParam("photoId") Long photoId,
            @RequestParam("spotId") Long spotId,
            RedirectAttributes redirectAttributes,
            Model model) {

        spotPhotoService.delete(photoId);
        redirectAttributes.addFlashAttribute("message", "画像を削除しました");

        return "redirect:/admin/spot/" + spotId + "/photo";
    }

}
