package com.example.nagahoribashi_walk.service;

import com.example.nagahoribashi_walk.dto.NavSubCategory;
import com.example.nagahoribashi_walk.dto.SidebarDTO;
import com.example.nagahoribashi_walk.entity.SubCategory;

/**
 * サブカテゴリ関連サービスのインターフェース
 * 
 * @author 大谷
 */
public interface SubCategoryService {

    /** 新規サブカテゴリを登録 */
    NavSubCategory getById(Long subCategoryId);

    /** サイドバー用DTOを取得 */
    SidebarDTO getSidebarDTO(Long subCategoryId);

    /** サブカテゴリの表示順を並べ替える */
    void reorderSubCategory(Long id, String direction);

    /** 新規サブカテゴリを登録 */
    void insertSubCategory(SubCategory subCategory);

    /** サブカテゴリを更新 */
    void updateSubCategory(SubCategory subCategory);

    /** サブカテゴリを削除 */
    void deleteSubCategory(Long id);
}
