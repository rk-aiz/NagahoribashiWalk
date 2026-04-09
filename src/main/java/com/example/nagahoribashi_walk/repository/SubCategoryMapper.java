package com.example.nagahoribashi_walk.repository;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.nagahoribashi_walk.dto.NavSubCategory;
import com.example.nagahoribashi_walk.entity.SubCategory;

/**
 * sub_categoriesテーブルに対応したMapperのインターフェース
 * 
 * @author 大谷
 */
@Mapper
public interface SubCategoryMapper {
    
    /** IDからサブカテゴリ(DTO)を取得する */
    Optional<NavSubCategory> findById(Long id);

    /** サブカテゴリIDから兄弟サブカテゴリ(DTO)を取得する */
    List<NavSubCategory> findSiblings(@Param("subCategoryId") Long subCategoryId);

    /** カテゴリIDからサブカテゴリ(DTO)を取得する */
    List<NavSubCategory> findByCategoryId(@Param("categoryId") Long categoryId);

    /** IDからサブカテゴリ(Entity)を取得する */
    Optional<SubCategory> findEntityById(Long id);

    /** カテゴリIDに対応するサブカテゴリ一覧を取得する */
    List<SubCategory> findEntityByCategoryId(Long categoryId);
    
    /** カテゴリIDと表示順でサブカテゴリを取得する */
    Optional<SubCategory> findEntityByCategoryIdAndDisplayOrder(
            @Param("categoryId") Long categoryId,
            @Param("displayOrder") Integer displayOrder);

    /** カテゴリIDに対応する「その他」サブカテゴリを取得する */
    Optional<SubCategory> findDefaultByCategoryId(Long categoryId);

    /** 追加 */
    void insert(SubCategory subCategory);

    /** 更新(名前のみ) */
	void update(SubCategory subCategory);

    /** 表示順を更新 */
    void updateDisplayOrder(@Param("id") Long id, @Param("displayOrder") Integer displayOrder);

    /** デフォルトフラグを更新 */
    void updateIsDefault(@Param("id") Long id, @Param("isDefault") Boolean isDefault);

    /** 削除 */
    void delete(@Param("id") Long id);

}
