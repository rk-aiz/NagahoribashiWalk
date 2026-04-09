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
		if (subCategory.getCategoryId() == null || subCategory.getCategoryId() == 0) {
	        subCategory.setCategoryId(null); 
	    }
		subCategoryMapper.insert(subCategory);
		
	}

	@Override
	public void deleteSubCategory(Long id) {
		subCategoryMapper.delete(id);
		
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
	public void updateSubCategory(SubCategory subCategory) {
		
		subCategoryMapper.update(subCategory);
	}

	@Override
	public void reorderSubCategory(Long id, String direction) {

		SubCategory subCategory1 = subCategoryMapper.findEntityById(id).orElseThrow();
		SubCategory subCategory2 = switch(direction) {
			case "up" -> subCategoryMapper.findEntityByCategoryIdAndDisplayOrder(
						subCategory1.getCategoryId(), 
						subCategory1.getDisplayOrder() - 1).orElseThrow();
			default -> subCategoryMapper.findEntityByCategoryIdAndDisplayOrder(
						subCategory1.getCategoryId(), 
						subCategory1.getDisplayOrder() + 1).orElseThrow();
		};

		// TODO : subCategory2がnullの場合のフォールバック処理

		subCategoryMapper.updateDisplayOrder(subCategory1.getId(), subCategory2.getDisplayOrder());
		subCategoryMapper.updateDisplayOrder(subCategory2.getId(), subCategory1.getDisplayOrder());
	}

}
