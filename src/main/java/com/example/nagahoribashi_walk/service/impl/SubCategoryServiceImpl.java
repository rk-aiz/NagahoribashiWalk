package com.example.nagahoribashi_walk.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagahoribashi_walk.dto.NavCategory;
import com.example.nagahoribashi_walk.dto.SidebarDTO;
import com.example.nagahoribashi_walk.dto.SubCategoryDTO;
import com.example.nagahoribashi_walk.repository.CategoryMapper;
import com.example.nagahoribashi_walk.repository.SubCategoryMapper;
import com.example.nagahoribashi_walk.service.SubCategoryService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SubCategoryServiceImpl implements SubCategoryService {

    private final CategoryMapper categoryMapper;
    private final SubCategoryMapper subCategoryMapper;

    @Override
    public SubCategoryDTO getById(Long subCategoryId) {
        return subCategoryMapper.findById(subCategoryId)
                .orElseThrow();
    }

    @Override
    public SidebarDTO getSidebarDTO(Long subCategoryId) {

        return new SidebarDTO(
                categoryMapper.findFlatAllNavCategories(),
                subCategoryMapper.findSiblings(subCategoryId),
                getById(subCategoryId).getCategoryId(),
                subCategoryId);
    }

}
