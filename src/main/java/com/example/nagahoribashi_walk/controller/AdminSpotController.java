package com.example.nagahoribashi_walk.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagahoribashi_walk.dto.AdminSpotRow;
import com.example.nagahoribashi_walk.entity.Spot;
import com.example.nagahoribashi_walk.form.SpotForm;
import com.example.nagahoribashi_walk.service.AdminSpotService;
import com.example.nagahoribashi_walk.service.CategoryService;

import lombok.RequiredArgsConstructor;

/**
 * 管理者スポット編集画面用コントローラー
 *
 * @author 海津, 池田
 */
@Controller
@RequiredArgsConstructor
public class AdminSpotController {

    private final AdminSpotService adminSpotService;
    private final CategoryService categoryService;

    /**
     * 管理者用スポット一覧画面
     */
    @GetMapping("/admin/spot/list")
    public String list(
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "sort", defaultValue = "id_asc") String sort,
            @PageableDefault(size = 15) Pageable pageable,
            Model model) {

        Page<AdminSpotRow> spotPages = adminSpotService.searchForAdmin(keyword, sort, pageable);

        if (spotPages.isEmpty() && pageable.getPageNumber() > 0) {
            int lastPage = Math.max(0, spotPages.getTotalPages() - 1);
            return "redirect:/admin/spot/list?page=" + lastPage
                    + "&keyword=" + URLEncoder.encode(keyword, StandardCharsets.UTF_8)
                    + "&sort=" + sort;
        }

        model.addAttribute("spotPages", spotPages);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sort", sort);
        return "admin/spot/list";
    }

    /**
     * 管理者用スポット新規登録画面
     */
    @GetMapping("/admin/spot/new")
    public String showNew(Model model) {

        // カテゴリ再読み込み経由でない場合のみ新規フォームを生成
        if (!model.containsAttribute("spotForm")) {
            SpotForm form = new SpotForm();
            form.setIsNew(true);
            model.addAttribute("spotForm", form);
        }

        model.addAttribute("dropDownCategories",
                categoryService.getAllForAdmin());
        return "admin/spot/edit";
    }

    /**
     * 管理者用スポット更新画面
     */
    @GetMapping("/admin/spot/edit/{spotId}")
    public String showEdit(
            @PathVariable("spotId") Long spotId,
            Model model) {

        // カテゴリ再読み込み経由でない場合のみDBからフォームを生成
        if (!model.containsAttribute("spotForm")) {
            SpotForm form = new SpotForm();
            Spot spot = adminSpotService.getByIdForAdmin(spotId);
            BeanUtils.copyProperties(spot, form);
            model.addAttribute("spotForm", form);
        }

        model.addAttribute("dropDownCategories",
                categoryService.getAllForAdmin());
        return "admin/spot/edit";
    }

    /**
     * スポット新規登録
     */
    @PostMapping("/admin/spot/add")
    public String register(
            @Validated SpotForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        // バリデーションエラー
        if (bindingResult.hasErrors()) {
            model.addAttribute("dropDownCategories",
                    categoryService.getAllForAdmin());
            return "admin/spot/edit";
        }

        // FromをEntityに詰め替え
        Spot spot = new Spot();
        BeanUtils.copyProperties(form, spot);

        adminSpotService.addSpot(spot);

        // フラッシュメッセージを設定
        redirectAttributes.addFlashAttribute("message", "新規スポットが登録されました。");
        return "redirect:/spot/" + spot.getId();
    }

    /**
     * スポット更新
     */
    @PostMapping("/admin/spot/update")
    public String update(
            @Validated SpotForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        // バリデーションエラー
        if (bindingResult.hasErrors()) {
            model.addAttribute("dropDownCategories",
                    categoryService.getAllForAdmin());
            return "admin/spot/edit";
        }

        // FromをEntityに詰め替え
        Spot spot = new Spot();
        BeanUtils.copyProperties(form, spot);

        adminSpotService.updateSpot(spot);

        // フラッシュメッセージを設定
        redirectAttributes.addFlashAttribute("message", "スポット情報が更新されました。");
        return "redirect:/admin/spot/edit/" + spot.getId();
    }

    /**
     * スポット削除
     */
    @PostMapping("/admin/spot/delete")
    public String softDelete(
            @RequestParam("spotId") Long spotId,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam("page") Integer page,
            RedirectAttributes redirectAttributes) {

        // スポットアップデート処理
        adminSpotService.softDelete(spotId);

        // フラッシュメッセージを設定
        redirectAttributes.addFlashAttribute("message", "スポット情報を削除しました。");
        redirectAttributes.addAttribute("page", page);
        if (keyword != null) {
            redirectAttributes.addAttribute("keyword", keyword);
        }

        return "redirect:/admin/spot/list";
    }

    /**
     * カテゴリ再読み込み（保存せずに入力値を保持したままリダイレクト）
     */
    @PostMapping("/admin/spot/refresh")
    public String refresh(
            SpotForm form,
            RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("spotForm", form);
        if (form.getIsNew()) {
            return "redirect:/admin/spot/new";
        } else {
            return "redirect:/admin/spot/edit/" + form.getId();
        }
    }
}
