package com.example.nagahoribashi_walk.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagahoribashi_walk.dto.AdminCategoryRow;
import com.example.nagahoribashi_walk.dto.NavCategory;
import com.example.nagahoribashi_walk.dto.NavSubCategory;
import com.example.nagahoribashi_walk.dto.SidebarDTO;
import com.example.nagahoribashi_walk.entity.Category;
import com.example.nagahoribashi_walk.entity.SubCategory;
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
        categoryMapper.insert(category);
    }

    @Override
    public void updateCategory(Category category) {
    	categoryMapper.update(category);
    }

    // 削除
    @Override
    public void deleteCategory(Long id) {

        // 「その他」サブカテゴリを削除
    	SubCategory defaultSubCategory = subCategoryMapper.findDefaultByCategoryId(id).orElseThrow();
    	subCategoryMapper.updateIsDefault(defaultSubCategory.getId(), false);
        subCategoryMapper.delete(defaultSubCategory.getId());
        
        categoryMapper.delete(id);
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
		Category category1 = categoryMapper.findEntityById(id).orElseThrow();
		Category category2 = switch(direction) {
			case "up" -> categoryMapper.findByDisplayOrder(
						category1.getDisplayOrder() - 1).orElseThrow();
			default -> categoryMapper.findByDisplayOrder(
						category1.getDisplayOrder() + 1).orElseThrow();
		};

        // TODO : category2がnullの場合のフォールバック処理

		categoryMapper.updateDisplayOrder(category1.getId(), category2.getDisplayOrder());
		categoryMapper.updateDisplayOrder(category2.getId(), category1.getDisplayOrder());
    }

}
