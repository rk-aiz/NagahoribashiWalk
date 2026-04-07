package com.example.nagahoribashi_walk.repository;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import com.example.nagahoribashi_walk.dto.NavSubCategory;
import com.example.nagahoribashi_walk.entity.SubCategory;

/**
 * sub_categoriesテーブルに対応したMapperのインターフェース
 * 
 */
@Mapper
public interface SubCategoryMapper {
    
    //大谷記載
    //追加
    void insertSubCategory(SubCategory subCategory);
    
    //削除
    void deleteSubCategory(@Param("id") Long id);

    Optional<NavSubCategory> findById(Long id);

    List<NavSubCategory> findSiblings(@Param("subCategoryId") Long subCategoryId);

    List<NavSubCategory> findByCategoryId(@Param("categoryId") Long categoryId);

}
