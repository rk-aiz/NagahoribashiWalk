package com.example.nagahoribashi_walk.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagahoribashi_walk.dto.NavSubCategory;
import com.example.nagahoribashi_walk.dto.SidebarDTO;
import com.example.nagahoribashi_walk.entity.SubCategory;
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
    public NavSubCategory getById(Long subCategoryId) {
        return subCategoryMapper.findById(subCategoryId)
                .orElseThrow();
    }
    
	@Override
	public void insertSubCategory(SubCategory subCategory) {
		subCategoryMapper.insertSubCategory(subCategory);
		
	}

	@Override
	public void deleteSubCategory(Long id) {
		subCategoryMapper.deleteSubCategory(id);
		
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
