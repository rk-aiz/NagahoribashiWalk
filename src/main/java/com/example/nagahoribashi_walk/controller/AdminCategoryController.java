package com.example.nagahoribashi_walk.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagahoribashi_walk.entity.Category;
import com.example.nagahoribashi_walk.entity.SubCategory;
import com.example.nagahoribashi_walk.form.CategoryForm;
import com.example.nagahoribashi_walk.form.SubCategoryForm;
import com.example.nagahoribashi_walk.service.CategoryService;
import com.example.nagahoribashi_walk.service.SubCategoryService;

import lombok.RequiredArgsConstructor;

/**
 * 管理者カテゴリ編集画面用コントローラー
 *
 * @author 海津, 大谷
 */
@Controller
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;

    @GetMapping("/admin/category/list")
    public String list(Model model) {

        model.addAttribute("categories",
                categoryService.findAllForAdmin());
        model.addAttribute("subCategoryForm", new SubCategoryForm());
        model.addAttribute("categoryForm", new CategoryForm());
        return "/admin/category/list";
    }

    /**
     * 親カテゴリーの追加
     */
    @PostMapping("/admin/category/add")
    public String addCategory(
            @Validated CategoryForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories",
                    categoryService.findAllForAdmin());
            model.addAttribute("subCategoryForm", new CategoryForm());
            return "/admin/category/list";
        }

        // Categoryエンティティを作成
        Category category = new Category();
        BeanUtils.copyProperties(form, category);

        // カテゴリ登録処理理
        categoryService.insertCategory(category);

        redirectAttributes.addFlashAttribute("message", "カテゴリーを追加しました。");
        return "redirect:/admin/category/list";
    }

    // 親カテゴリー名の更新
    @PostMapping("/admin/category/update")
    public String updateCategory(
            @Validated CategoryForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories",
                    categoryService.findAllForAdmin());
            model.addAttribute("subCategoryForm", new CategoryForm());
            return "/admin/category/list";
        }

        // Categoryエンティティを作成
        Category category = new Category();
        BeanUtils.copyProperties(form, category);

        // カテゴリ更新処理
        categoryService.updateCategory(category);

        redirectAttributes.addFlashAttribute("message", "カテゴリー名を更新しました。");
        return "redirect:/admin/category/list";
    }

    /**
     * 子カテゴリー（サブカテゴリー）の追加
     */
    @PostMapping("/admin/subcategory/add")
    public String addSubCategory(
            @Validated SubCategoryForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories",
                    categoryService.findAllForAdmin());
            model.addAttribute("categoryForm", new CategoryForm());
            return "/admin/category/list";
        }

        // SubCategoryエンティティを作成
        SubCategory subCategory = new SubCategory();
        BeanUtils.copyProperties(form, subCategory);

        // サブカテゴリ登録処理理
        subCategoryService.insertSubCategory(subCategory);

        redirectAttributes.addFlashAttribute("message", "サブカテゴリーを追加しました。");
        return "redirect:/admin/category/list";
    }

    /**
     * サブカテゴリーの個別削除
     */
    @PostMapping("/admin/subcategory/remove/{id}")
    public String removeSubCategory(
            @PathVariable("id") Long id,
            RedirectAttributes redirectAttributes) {
        subCategoryService.deleteSubCategory(id);
        redirectAttributes.addFlashAttribute("message", "サブカテゴリーを削除しました。");
        return "redirect:/admin/category/list";
    }
}
