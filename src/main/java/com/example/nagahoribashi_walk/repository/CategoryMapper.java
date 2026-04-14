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

    /** IDからカテゴリ(閲覧系)を取得 */
    Optional<NavCategory> findById(Long id);

    /** カテゴリ一覧(閲覧系) */
    List<NavCategory> findAllNavCategories();

    /** カテゴリ一覧(閲覧系・サブカテゴリなし) */
    List<NavCategory> findFlatAllNavCategories();

    /** 管理者用全件取得 */
    List<AdminCategoryRow> findAllForAdmin();

    /** エンティティを全件取得 */
    List<Category> findAllEntities();

    /** IDから、同カテゴリ内表示昇順でlimit数だけ取得する */
    List<Category> findFromById(@Param("id") Long id, @Param("limit") Integer limit);

    /** IDから、同カテゴリ内表示降順でlimit数だけ取得する */
    List<Category> findUpToById(@Param("id") Long id, @Param("limit") Integer limit);

    /** カテゴリ名が既に存在するか確認する */
    boolean existsByCategoryName(@Param("name") String name);

    /** カテゴリ名が既に存在するか確認する(指定のIDを除く) */
    boolean existsByCategoryNameExcludingId(@Param("name") String name, @Param("id") Long id);

    /** 追加 */
    void insert(Category category);

    /** 更新 */
    void update(Category category);

    /** 表示順を更新 */
    void updateDisplayOrder(@Param("id") Long id, @Param("displayOrder") Integer displayOrder);

    /** 表示順を一括更新 */
    void bulkUpdateDisplayOrder(@Param("categories") List<Category> categories);

    /** 削除 */
    void delete(@Param("id") Long id);

}
