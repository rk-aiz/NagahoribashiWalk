package com.example.nagahoribashi_walk.repository;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.example.nagahoribashi_walk.dto.AdminCategoryRow;
import com.example.nagahoribashi_walk.dto.NavCategory;

/**
 * categoriesテーブルに対応したMapperのインターフェース
 * 
 * @author 海津
 */
@Mapper
public interface CategoryMapper {

    Optional<NavCategory> findById(Long id);

    List<NavCategory> findAllNavCategories();

    List<NavCategory> findFlatAllNavCategories();

    List<AdminCategoryRow> findAllForAdmin();

}
