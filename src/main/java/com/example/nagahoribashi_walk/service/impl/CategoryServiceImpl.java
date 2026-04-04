package com.example.nagahoribashi_walk.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagahoribashi_walk.dto.CategoryDTO;
import com.example.nagahoribashi_walk.dto.NavCategory;
import com.example.nagahoribashi_walk.dto.NavSubCategory;
import com.example.nagahoribashi_walk.dto.SidebarDTO;
import com.example.nagahoribashi_walk.repository.CategoryMapper;
import com.example.nagahoribashi_walk.repository.SubCategoryMapper;
import com.example.nagahoribashi_walk.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;
    private final SubCategoryMapper subCategoryMapper;

    @Override
    public List<NavCategory> getAllNavCategories() {
        return categoryMapper.findAllNavCategories();
    }

    @Override
    public CategoryDTO getById(Long categoryId) {
        return categoryMapper.findById(categoryId).orElseThrow();
    }

    @Override
    public SidebarDTO getSidebarDTO(Long categoryId) {

        List<NavSubCategory> subCategories = subCategoryMapper.findByCategoryId(categoryId);

        return new SidebarDTO(
                categoryMapper.findFlatAllNavCategories(),
                subCategories,
                categoryId, null);
    }
}
