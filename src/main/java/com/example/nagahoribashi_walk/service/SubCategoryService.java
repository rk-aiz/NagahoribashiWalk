package com.example.nagahoribashi_walk.service;

import java.util.List;
import java.util.Map;

import com.example.nagahoribashi_walk.entity.SubCategory;

/**
 * サブカテゴリ関連サービスのインターフェース
 */
public interface SubCategoryService {

    Map<Long, List<SubCategory>> findAllGroupedByCategory();
    
    //大谷記載
    //追加
    void insertSubCategory(SubCategory subCategory);
    
    //削除
    void deleteSubCategory(Long id);
}
