package com.example.nagahoribashi_walk.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagahoribashi_walk.dto.CategoryDTO;
import com.example.nagahoribashi_walk.dto.NavCategory;
import com.example.nagahoribashi_walk.repository.CategoryMapper;
import com.example.nagahoribashi_walk.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;
    private final CategoryDTO CATEGORY_OTHER = new CategoryDTO(null, "その他");

    @Override
    public List<NavCategory> getAllNavCategories() {
        return categoryMapper.findAllNavCategories();
    }

    @Override
    public CategoryDTO getById(Long categoryId) {
        return categoryMapper.findById(categoryId).orElse(CATEGORY_OTHER);
    }
}
