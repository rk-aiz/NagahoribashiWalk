package com.example.nagahoribashi_walk.repository;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.nagahoribashi_walk.dto.AdminCategoryRow;
import com.example.nagahoribashi_walk.dto.NavCategory;
import com.example.nagahoribashi_walk.entity.Category;

/**
 * categoriesテーブルに対応したMapperのインターフェース
 * 
 * @author 海津, 大谷
 */
@Mapper
public interface CategoryMapper {

    List<NavCategory> findAll();

    Optional<NavCategory> findById(Long id);

    List<NavCategory> findAllNavCategories();

    List<NavCategory> findFlatAllNavCategories();

    List<AdminCategoryRow> findAllForAdmin();

    List<Category> findAllEntities();

    List<Category> findFromById(@Param("id") Long id, @Param("limit") Integer limit);

    List<Category> findUpToById(@Param("id") Long id, @Param("limit") Integer limit);

    boolean existsByCategoryName(@Param("name") String name);

    /** 追加 */
    void insert(Category category);

    void update(Category category);

    void updateDisplayOrder(@Param("id") Long id, @Param("displayOrder") Integer displayOrder);

    void bulkUpdateDisplayOrder(@Param("categories") List<Category> categories);

    /** 削除 */
    void delete(@Param("id") Long id);

}
