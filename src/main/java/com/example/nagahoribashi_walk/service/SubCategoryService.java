package com.example.nagahoribashi_walk.service;

import java.util.List;
import java.util.Map;

import com.example.nagahoribashi_walk.dto.NavSubCategory;
import com.example.nagahoribashi_walk.dto.SidebarDTO;
import com.example.nagahoribashi_walk.entity.SubCategory;

/**
 * サブカテゴリ関連サービスのインターフェース
 */
public interface SubCategoryService {

    //大谷記載
    //追加
    void insertSubCategory(SubCategory subCategory);
    
    //削除
    void deleteSubCategory(Long id);
    
    NavSubCategory getById(Long subCategoryId);

    SidebarDTO getSidebarDTO(Long subCategoryId);

	Map<Long, List<SubCategory>> findAllGroupedByCategory();

	void updateSubCategory(SubCategory subCategory);
}
