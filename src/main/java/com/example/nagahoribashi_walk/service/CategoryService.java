package com.example.nagahoribashi_walk.service;

import java.util.List;

import com.example.nagahoribashi_walk.dto.NavCategory;
import com.example.nagahoribashi_walk.dto.SidebarDTO;

/**
 * カテゴリ関連サービスのインターフェース
 */
public interface CategoryService {

    /** IDからカテゴリ（閲覧系）を取得 */
    NavCategory getById(Long categoryId);

    List<NavCategory> getAllNavCategories();

    SidebarDTO getSidebarDTO(Long categoryId);
}
