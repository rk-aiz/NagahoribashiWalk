package com.example.nagahoribashi_walk.service;

import java.util.List;

import com.example.nagahoribashi_walk.dto.CategoryDTO;
import com.example.nagahoribashi_walk.dto.NavCategory;

/**
 * カテゴリ関連サービスのインターフェース
 */
public interface CategoryService {

    List<NavCategory> getAllNavCategories();

    CategoryDTO getById(Long categoryId);
}
