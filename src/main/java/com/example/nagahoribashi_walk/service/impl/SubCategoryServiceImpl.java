package com.example.nagahoribashi_walk.service.impl;

import java.util.List;

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
    public SidebarDTO getSidebarDTO(Long subCategoryId) {

        return new SidebarDTO(
                categoryMapper.findFlatAllNavCategories(),
                subCategoryMapper.findSiblings(subCategoryId),
                getById(subCategoryId).getCategoryId(),
                subCategoryId);
    }

    @Override
    public void reorderSubCategory(Long id, String direction) {
        List<SubCategory> subCategoriesForReplace = switch (direction) {
            case "up" -> subCategoryMapper.findUpToById(id, 2);
            default -> subCategoryMapper.findFromById(id, 2);
        };

        if (subCategoriesForReplace.size() == 2) {
            SubCategory subCategory1 = subCategoriesForReplace.getFirst();
            SubCategory subCategory2 = subCategoriesForReplace.getLast();
            subCategoryMapper.updateDisplayOrder(subCategory1.getId(), subCategory2.getDisplayOrder());
            subCategoryMapper.updateDisplayOrder(subCategory2.getId(), subCategory1.getDisplayOrder());
        }
    }

    @Override
    public void insertSubCategory(SubCategory subCategory) {
        if (subCategory.getCategoryId() == null || subCategory.getCategoryId() == 0) {
            subCategory.setCategoryId(null);
        }
        subCategoryMapper.insert(subCategory);
    }

    @Override
    public void updateSubCategory(SubCategory subCategory) {
        subCategoryMapper.update(subCategory);
    }

    @Override
    public void deleteSubCategory(Long id) {

        subCategoryMapper.findEntityById(id).ifPresent(sc -> {
            subCategoryMapper.delete(id);
            normalizeDisplayOrderByCategoryId(sc.getCategoryId());
        });
    }

    private void normalizeDisplayOrderByCategoryId(Long categoryId) {
        List<SubCategory> subCategories = subCategoryMapper.findEntitiesByCategoryId(categoryId)
                .stream().filter(sc -> !sc.isDefault()).toList();

        boolean requireUpdate = false;
        if (!subCategories.isEmpty()) {
            for (int i = 0; i < subCategories.size(); i++) {
                SubCategory sc = subCategories.get(i);
                if (!sc.getDisplayOrder().equals(i + 1)) {
                    sc.setDisplayOrder(i + 1);
                    requireUpdate = true;
                }
            }
        }

        if (requireUpdate) {
            subCategoryMapper.bulkUpdateDisplayOrder(subCategories);
        }
    }
}
