package com.example.nagahoribashi_walk.repository;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.example.nagahoribashi_walk.dto.SubCategoryDTO;

/**
 * sub_categoriesテーブルに対応したMapperのインターフェース
 * 
 */
@Mapper
public interface SubCategoryMapper {

    Optional<SubCategoryDTO> findById(Long id);

}
