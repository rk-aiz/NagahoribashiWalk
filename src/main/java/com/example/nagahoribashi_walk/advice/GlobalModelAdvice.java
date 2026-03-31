package com.example.nagahoribashi_walk.advice;

import java.util.List;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.nagahoribashi_walk.dto.NavCategory;
import com.example.nagahoribashi_walk.service.CategoryService;

import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAdvice {

    private final CategoryService categoryService;

    @ModelAttribute("navCategories")
    public List<NavCategory> navCategories() {
        return categoryService.findAll();
    }
}
