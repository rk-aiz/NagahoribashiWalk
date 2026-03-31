package com.example.nagahoribashi_walk.advice;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.nagahoribashi_walk.dto.NavCategory;
import com.example.nagahoribashi_walk.entity.SubCategory;
import com.example.nagahoribashi_walk.service.CategoryService;
import com.example.nagahoribashi_walk.service.SubCategoryService;

import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAdvice {

    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;

    @ModelAttribute("navCategories")
    public List<NavCategory> navCategories() {
        return categoryService.findAll();
    }

    @ModelAttribute("navSubCategoryMap")
    public Map<Long, List<SubCategory>> navSubCategoryMap() {
        return subCategoryService.findAllGroupedByCategory();
    }
}
