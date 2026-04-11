package com.example.nagahoribashi_walk.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagahoribashi_walk.dto.AdminCategoryRow;
import com.example.nagahoribashi_walk.exception.CategoryAlreadyExistsException;
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

    @Override
    public List<NavCategory> findAll() {
        return categoryMapper.findAll();
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
        List<Category> categoriesForReplace = switch (direction) {
            case "up" -> categoryMapper.findUpToById(id, 2);
            default -> categoryMapper.findFromById(id, 2);
        };

        if (categoriesForReplace.size() == 2) {
            Category category1 = categoriesForReplace.getFirst();
            Category category2 = categoriesForReplace.getLast();
            categoryMapper.updateDisplayOrder(category1.getId(), category2.getDisplayOrder());
            categoryMapper.updateDisplayOrder(category2.getId(), category1.getDisplayOrder());
        }
    }

    /** 新規カテゴリの登録 */ 
    @Override
    public void insertCategory(Category category) {
        if (categoryMapper.existsByCategoryName(category.getName())) {
            throw new CategoryAlreadyExistsException("カテゴリ名が既に存在します。");
        }
        categoryMapper.insert(category);
    }

    @Override
    public void updateCategory(Category category) {
        if (categoryMapper.existsByCategoryName(category.getName())) {
            throw new CategoryAlreadyExistsException("カテゴリ名が既に存在します。");
        }
        categoryMapper.update(category);
    }

    // 削除
    @Override
    public void deleteCategory(Long id) {
        // spots の移動・デフォルトサブカテゴリの削除・非デフォルトサブカテゴリの移動は
        // fallback_on_category_delete DB トリガーが一括処理する
        categoryMapper.delete(id);

        normalizeDisplayOrder();
    }

    private void normalizeDisplayOrder() {
        List<Category> categories = categoryMapper.findAllEntities();
        boolean requireUpdate = false;
        if (!categories.isEmpty()) {
            for (int i = 0; i < categories.size(); i++) {
                Category c = categories.get(i);
                if (!c.getDisplayOrder().equals(i + 1)) {
                    c.setDisplayOrder(i + 1);
                    requireUpdate = true;
                }
            }
        }

        if (requireUpdate) {
            categoryMapper.bulkUpdateDisplayOrder(categories);
        }
    }

}
