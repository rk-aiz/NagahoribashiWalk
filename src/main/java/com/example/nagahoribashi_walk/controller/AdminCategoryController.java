package com.example.nagahoribashi_walk.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagahoribashi_walk.dto.NavCategory;
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
@RequestMapping("/admin/categories")
public class AdminCategoryController {

	 private final CategoryService categoryService;
	 private final SubCategoryService subCategoryService;

	    @GetMapping
	    public String index(Model model) {
	        List<NavCategory> categories = categoryService.findAll();
	        model.addAttribute("categories", categories);
	        
	        // 新規登録用空オブジェクト
	        model.addAttribute("categoryForm", new Category(null, null, null));
	        model.addAttribute("subCategoryForm", new SubCategory());
	        
	        return "admin/categories/index";
	    }

	    /**
	     * 親カテゴリーの追加
	     */
	    @PostMapping("/parent/create")
	    public String createParent(@ModelAttribute Category category, RedirectAttributes redirectAttributes) {
	        categoryService.insertCategory(category);
	        redirectAttributes.addFlashAttribute("successMessage", "カテゴリーを追加しました。");
	        return "redirect:/admin/categories";
	    }

	    /**
	     * 子カテゴリー（サブカテゴリー）の追加
	     */
	    @PostMapping("/child/create")
	    public String createChild(@ModelAttribute SubCategory subCategory, RedirectAttributes redirectAttributes) {
	        subCategoryService.insertSubCategory(subCategory);
	        redirectAttributes.addFlashAttribute("successMessage", "サブカテゴリーを追加しました。");
	        return "redirect:/admin/categories";
	    }

	    /**
	     * サブカテゴリーの個別削除
	     */
	    @PostMapping("/child/delete/{id}")
	    public String deleteChild(@PathVariable Long id, RedirectAttributes redirectAttributes) {
	        subCategoryService.deleteSubCategory(id);
	        redirectAttributes.addFlashAttribute("successMessage", "サブカテゴリーを削除しました。");
	        return "redirect:/admin/categories";	 
}
}
