package com.example.nagahoribashi_walk.service;

import java.util.List;
import java.util.Map;

import com.example.nagahoribashi_walk.entity.SubCategory;

/**
 * サブカテゴリ関連サービスのインターフェース
 */
public interface SubCategoryService {

    Map<Long, List<SubCategory>> findAllGroupedByCategory();
}
