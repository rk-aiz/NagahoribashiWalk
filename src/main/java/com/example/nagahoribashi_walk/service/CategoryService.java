package com.example.nagahoribashi_walk.service;

import java.util.List;

import com.example.nagahoribashi_walk.dto.NavCategory;
import com.example.nagahoribashi_walk.entity.Category;

/**
 * カテゴリ関連サービスのインターフェース
 */
public interface CategoryService {

    List<NavCategory> findAll();
    
    
    //大谷記載
    //追加
    void insertCategory(Category category);

    //削除
    void deleteCategory(Long id);
}
