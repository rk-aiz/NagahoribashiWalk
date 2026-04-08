package com.example.nagahoribashi_walk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagahoribashi_walk.entity.Category;
import com.example.nagahoribashi_walk.entity.SubCategory;
import com.example.nagahoribashi_walk.service.CategoryService;
import com.example.nagahoribashi_walk.service.SubCategoryService;

import lombok.RequiredArgsConstructor;

/**
 * 
 * @author 海津
 */
@Controller
@RequiredArgsConstructor
public class AdminCategoryController {

	 private final CategoryService categoryService;
	 private final SubCategoryService subCategoryService;


	    @GetMapping("/admin/category/list")
	    public String list(Model model) {
	        model.addAttribute("categoryForm", new Category());
	        return "admin/category/list";
	    }

	    /**
	     * 親カテゴリーの追加
	     */
	    @PostMapping("/admin/category/add")
	    public String addCategory(@ModelAttribute("categoryForm") Category category, RedirectAttributes redirectAttributes) {
	        categoryService.insertCategory(category);
	        redirectAttributes.addFlashAttribute("successMessage", "カテゴリーを追加しました。");
	        return "redirect:/admin/categories";
	    }

	    /**
	     * 子カテゴリー（サブカテゴリー）の追加
	     */
	    @PostMapping("/admin/subcategory/add")
	    public String addSubCategory(@ModelAttribute SubCategory subCategory, RedirectAttributes redirectAttributes) {
	        subCategoryService.insertSubCategory(subCategory);
	        redirectAttributes.addFlashAttribute("successMessage", "サブカテゴリーを追加しました。");
	        return "redirect:/admin/categories";
	    }

	    /**
	     * サブカテゴリーの個別削除
	     */
	    @PostMapping("/admin/subcategory/remove/{id}")
	    public String removeSubCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
	        subCategoryService.deleteSubCategory(id);
	        redirectAttributes.addFlashAttribute("successMessage", "サブカテゴリーを削除しました。");
	        return "redirect:/admin/categories";
	        
	    }
}
