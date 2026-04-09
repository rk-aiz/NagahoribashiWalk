package com.example.nagahoribashi_walk.service.impl;

import java.util.List;
import java.util.Map;

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
		if (subCategory.getCategoryId() == null || subCategory.getCategoryId() == 0) {
	        subCategory.setCategoryId(null); 
	    }
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

	@Override
	public Map<Long, List<SubCategory>> findAllGroupedByCategory() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public void updateSubCategory(SubCategory subCategory) {
		
		subCategoryMapper.updateSubCategory(subCategory);
	}

	@Override
	public void reorderSubCategory(Long id, String direction) {
	    SubCategory current = subCategoryMapper.findByIdEntity(id);
	    
	    List<SubCategory> siblings = subCategoryMapper.findByCategoryIdEntity(current.getCategoryId());
	    	    
	    for (int i = 0; i < siblings.size(); i++) {
	        subCategoryMapper.updateDisplayOrder(siblings.get(i).getId(), i + 1);
	    }
	    	    
	    for (int i = 0; i < siblings.size(); i++) {
	        if (siblings.get(i).getId().equals(id)) {
	            int targetIdx = "up".equals(direction) ? i - 1 : i + 1;
	            if (targetIdx >= 0 && targetIdx < siblings.size()) {
	                subCategoryMapper.updateDisplayOrder(siblings.get(i).getId(), targetIdx + 1);
	                subCategoryMapper.updateDisplayOrder(siblings.get(targetIdx).getId(), i + 1);
	            }
	            break;
	        }
	    }
	}

}
