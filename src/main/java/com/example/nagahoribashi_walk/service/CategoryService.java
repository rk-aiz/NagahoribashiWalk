package com.example.nagahoribashi_walk.service;

import java.util.List;

import com.example.nagahoribashi_walk.dto.AdminCategoryRow;
import com.example.nagahoribashi_walk.dto.NavCategory;
import com.example.nagahoribashi_walk.dto.SidebarDTO;
import com.example.nagahoribashi_walk.entity.Category;

/**
 * カテゴリ関連サービスのインターフェース
 */
public interface CategoryService {

    List<NavCategory> findAll();

    // 大谷記載
    // 追加
    void insertCategory(Category category);

    // 削除
    void deleteCategory(Long id);

    // 更新
    void updateCategory(Category category);

    /** IDからカテゴリ（閲覧系）を取得 */
    NavCategory getById(Long categoryId);

    List<NavCategory> getAllNavCategories();

    SidebarDTO getSidebarDTO(Long categoryId);

    List<AdminCategoryRow> getAllAdminCategoryRows();

    List<AdminCategoryRow> findAllForAdmin();

}
