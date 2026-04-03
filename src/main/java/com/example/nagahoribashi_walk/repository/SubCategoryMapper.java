package com.example.nagahoribashi_walk.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

import com.example.nagahoribashi_walk.entity.SubCategory;

/**
 * sub_categoriesテーブルに対応したMapperのインターフェース
 * 
 */
@Mapper
public interface SubCategoryMapper {

    @Select("SELECT id, category_id, name FROM sub_categories ORDER BY name")
    List<SubCategory> findAll();
    
    //大谷記載
    //追加
    void insertSubCategory(SubCategory subCategory);
    
    //削除
    void deleteSubCategory(@Param("id") Long id);

}
