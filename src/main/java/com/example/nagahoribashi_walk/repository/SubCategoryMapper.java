package com.example.nagahoribashi_walk.repository;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.nagahoribashi_walk.dto.NavSubCategory;
import com.example.nagahoribashi_walk.dto.SubCategoryDTO;

/**
 * sub_categoriesテーブルに対応したMapperのインターフェース
 * 
 */
@Mapper
public interface SubCategoryMapper {

    Optional<SubCategoryDTO> findById(Long id);

    List<NavSubCategory> findSiblings(@Param("subCategoryId") Long subCategoryId);

    List<NavSubCategory> findByCategoryId(@Param("categoryId") Long categoryId);

}
