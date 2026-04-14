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

    /** IDからカテゴリ（閲覧系）を取得 */
    NavCategory getById(Long categoryId);

    /** カテゴリナビ用に全件取得 */
    List<NavCategory> getAllNavCategories();

    /** カテゴリIDから、サイドバー用のDTO取得 */
    SidebarDTO getSidebarDTO(Long categoryId);

    /** 管理者用全件取得 */
    List<AdminCategoryRow> getAllForAdmin();

    /** 表示順を並べ替え */
    void reorderCategory(Long id, String direction);

    /** 追加 */
    void insertCategory(Category category);

    /** 更新 */
    void updateCategory(Category category);

    /** 削除 */
    void deleteCategory(Long id);

}
