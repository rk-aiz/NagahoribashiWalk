package com.example.nagahoribashi_walk.service;

import com.example.nagahoribashi_walk.dto.NavSubCategory;
import com.example.nagahoribashi_walk.dto.SidebarDTO;

/**
 * サブカテゴリ関連サービスのインターフェース
 */
public interface SubCategoryService {

    NavSubCategory getById(Long subCategoryId);

    SidebarDTO getSidebarDTO(Long subCategoryId);
}
