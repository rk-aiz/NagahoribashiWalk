package com.example.nagahoribashi_walk.service;

import com.example.nagahoribashi_walk.dto.SidebarDTO;
import com.example.nagahoribashi_walk.dto.SubCategoryDTO;

/**
 * サブカテゴリ関連サービスのインターフェース
 */
public interface SubCategoryService {

    SubCategoryDTO getById(Long subCategoryId);

    SidebarDTO getSidebarDTO(Long subCategoryId);
}
