package com.example.nagahoribashi_walk.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import com.example.nagahoribashi_walk.dto.NavCategory;
import com.example.nagahoribashi_walk.entity.Category;

/**
 * categoriesテーブルに対応したMapperのインターフェース
 * 
 * @author 海津
 */
@Mapper
public interface CategoryMapper {

    List<NavCategory> findAll();
    
    //大谷記載
    //追加
    void insertCategory(Category category);

    //削除
    void deleteCategory(@Param("id") Long id);
}
