package com.example.nagahoribashi_walk.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagahoribashi_walk.dto.NavSubCategory;
import com.example.nagahoribashi_walk.dto.SidebarDTO;
import com.example.nagahoribashi_walk.entity.SubCategory;
import com.example.nagahoribashi_walk.exception.CategoryAlreadyExistsException;
import com.example.nagahoribashi_walk.exception.InvalidRequestException;
import com.example.nagahoribashi_walk.repository.CategoryMapper;
import com.example.nagahoribashi_walk.repository.SubCategoryMapper;
import com.example.nagahoribashi_walk.service.SubCategoryService;

import lombok.RequiredArgsConstructor;

/**
 * サブカテゴリ―サービスの実装クラス
 * 
 * @author 大谷、海津
 */
@Service
@Transactional
@RequiredArgsConstructor
public class SubCategoryServiceImpl implements SubCategoryService {

    private final CategoryMapper categoryMapper;
    private final SubCategoryMapper subCategoryMapper;

    /** IDからサブカテゴリを取得 */
    @Override
    public NavSubCategory getById(Long subCategoryId) {
        return subCategoryMapper.findById(subCategoryId)
                .orElseThrow();
    }

    /** サイドバー用DTOを取得 */
    @Override
    public SidebarDTO getSidebarDTO(Long subCategoryId) {

        return new SidebarDTO(
                categoryMapper.findFlatAllNavCategories(),
                subCategoryMapper.findSiblings(subCategoryId),
                getById(subCategoryId).getCategoryId(),
                subCategoryId);
    }

    /** サブカテゴリの表示順を並べ替える */
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

    /** 新規サブカテゴリを登録 */
    @Override
    public void insertSubCategory(SubCategory subCategory) {

        if (subCategory.getCategoryId() == null || subCategory.getCategoryId() == 0) {
            throw new InvalidRequestException("指定されたカテゴリIDが存在しません。");
        }

        if (subCategoryMapper.existsBySubCategoryNameAndCategoryId(
                subCategory.getName(), subCategory.getCategoryId())) {
            throw new CategoryAlreadyExistsException("サブカテゴリー名が既に存在します。");
        }

        subCategoryMapper.insert(subCategory);
    }

    /** サブカテゴリを更新 */
    @Override
    public void updateSubCategory(SubCategory subCategory) {

        if (subCategoryMapper.existsBySubCategoryNameAndCategoryId(
                subCategory.getName(), subCategory.getCategoryId())) {
            throw new CategoryAlreadyExistsException("サブカテゴリー名が既に存在します。");
        }
        subCategoryMapper.update(subCategory);
    }

    /** サブカテゴリを削除 */
    @Override
    public void deleteSubCategory(Long id) {

        subCategoryMapper.findEntityById(id).ifPresent(sc -> {
            subCategoryMapper.delete(id);
            normalizeDisplayOrderByCategoryId(sc.getCategoryId());
        });
    }

    /** カテゴリIDに対応するサブカテゴリの表示順を正規化する */
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
