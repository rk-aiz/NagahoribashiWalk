package com.example.nagahoribashi_walk.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagahoribashi_walk.dto.SaveImagesResult;
import com.example.nagahoribashi_walk.service.AdminSpotService;
import com.example.nagahoribashi_walk.service.SpotPhotoService;

import lombok.RequiredArgsConstructor;

/**
 * 管理者スポット画像管理用コントローラー
 */
@Controller
@RequiredArgsConstructor
public class AdminSpotPhotoController {

    private final AdminSpotService adminSpotService;
    private final SpotPhotoService spotPhotoService;

    /**
     * 画像管理画面を表示する
     */
    @GetMapping("/admin/spot/{spotId}/photo")
    public String showSpotPhoto(
            @PathVariable("spotId") Long spotId,
            Model model) {

        model.addAttribute("spot", adminSpotService.getByIdForAdmin(spotId));
        model.addAttribute("photos", spotPhotoService.getAllBySpotId(spotId));

        return "/admin/spot/photo";
    }

    /**
     * 画像をアップロード処理
     */
    @PostMapping("/admin/spot/{spotId}/photo/upload")
    public String uploadImages(
            @PathVariable("spotId") Long spotId,
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("displayOrder") Integer displayOrder,
            RedirectAttributes redirectAttributes) {

        // 画像を保存する
        SaveImagesResult result = spotPhotoService.saveImages(files, spotId, displayOrder);

        if (!result.savedFilenames().isEmpty()) {
            redirectAttributes.addFlashAttribute("message",
                    result.savedFilenames().size() + "件アップロードしました");
        }
        if (!result.errors().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    String.join(", ", result.errors()));
        }

        return "redirect:/admin/spot/" + spotId + "/photo";
    }

    /**
     * 画像削除処理
     */
    @PostMapping("/admin/spot/photo/delete")
    public String delete(
            @RequestParam("photoId") Long photoId,
            @RequestParam("spotId") Long spotId,
            RedirectAttributes redirectAttributes,
            Model model) {

        spotPhotoService.delete(photoId, spotId);

        // フラッシュメッセージを設定
        redirectAttributes.addFlashAttribute("message", "画像を削除しました");

        return "redirect:/admin/spot/" + spotId + "/photo";
    }

    /**
     * 画像順序入れ替え処理
     */
    @PostMapping("/admin/spot/{spotId}/photo/reorder")
    public String reorder(
            @PathVariable("spotId") Long spotId,
            @RequestParam("displayOrder1") Integer displayOrder1,
            @RequestParam("displayOrder2") Integer displayOrder2) {

        spotPhotoService.reorder(spotId, displayOrder1, displayOrder2);

        return "redirect:/admin/spot/" + spotId + "/photo";
    }

}
