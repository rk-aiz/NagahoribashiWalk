package com.example.nagahoribashi_walk.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagahoribashi_walk.dto.AdminCategoryRow;
import com.example.nagahoribashi_walk.dto.NavCategory;
import com.example.nagahoribashi_walk.dto.NavSubCategory;
import com.example.nagahoribashi_walk.dto.SidebarDTO;
import com.example.nagahoribashi_walk.entity.Category;
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
	private List<NavCategory> list;

    @Override
    public List<NavCategory> findAll() {
        return categoryMapper.findAll();
    }

    // 追加
    @Override
    public void insertCategory(Category category) {
        categoryMapper.insertCategory(category);
    }

    @Override
    public void updateCategory(Category category) {
    	categoryMapper.updateCategory(category);
    }

    // 削除
    @Override
    public void deleteCategory(Long id) {
    	subCategoryMapper.disableDefaultFlagByCategoryId(id);
    	subCategoryMapper.deleteSubCategoriesByCategoryId(id);
        categoryMapper.deleteCategory(id);
    }

    @Override
    public List<NavCategory> getAllNavCategories() {
        return categoryMapper.findAllNavCategories();
    }

    @Override
    public NavCategory getById(Long categoryId) {
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

    @Override
    public List<AdminCategoryRow> getAllAdminCategoryRows() {
        return categoryMapper.findAllForAdmin();
    }

    @Override
    public List<AdminCategoryRow> findAllForAdmin() {

        return categoryMapper.findAllForAdmin();
    }
    
    @Override
    public void reorderCategory(Long id, String direction) {
        // 現在の並び順で全カテゴリーを取得
        List<AdminCategoryRow> categories = categoryMapper.findAllForAdmin();
        
        
		for (int i = 0; i < list.size(); i++) {
            categoryMapper.updateDisplayOrder(list.get(i).getId(), i + 1);
            
		}
		
		for (int i = 0; i < list.size(); i++) {
	        if (list.get(i).getId() == id) {
	            int targetIdx = "up".equals(direction) ? i - 1 : i + 1;
	            
	            if (targetIdx >= 0 && targetIdx < list.size()) {
	                // 自分と相手のIDを特定
	                Long currentId = list.get(i).getId();
	                Long targetId = list.get(targetIdx).getId();
	                
	                // 番号を交換（i+1 と targetIdx+1）
	                categoryMapper.updateDisplayOrder(currentId, targetIdx + 1);
	                categoryMapper.updateDisplayOrder(targetId, i + 1);
	            }
	            break;
	        }
	    }
	}

}
