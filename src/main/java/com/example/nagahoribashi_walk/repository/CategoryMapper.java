package com.example.nagahoribashi_walk.repository;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import com.example.nagahoribashi_walk.dto.AdminCategoryRow;
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
    
    Optional<NavCategory> findById(Long id);

    List<NavCategory> findAllNavCategories();

    List<NavCategory> findFlatAllNavCategories();

    List<AdminCategoryRow> findAllForAdmin();

	void updateCategory(Category category);

}
