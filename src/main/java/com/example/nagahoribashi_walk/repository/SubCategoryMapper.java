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
    Optional<NavSubCategory> findById(@Param("id") Long id);

    /** サブカテゴリIDから兄弟サブカテゴリ(DTO)を取得する */
    List<NavSubCategory> findSiblings(@Param("subCategoryId") Long subCategoryId);

    /** カテゴリIDからサブカテゴリ(DTO)を取得する */
    List<NavSubCategory> findByCategoryId(@Param("categoryId") Long categoryId);

    /** カテゴリIDに対応するサブカテゴリ一覧を取得する */
    Optional<SubCategory> findEntityById(@Param("id") Long id);

    /** カテゴリIDに対応するサブカテゴリ一覧を取得する */
    List<SubCategory> findEntitiesByCategoryId(@Param("categoryId") Long categoryId);

    /** サブカテゴリIDから、同カテゴリ内表示昇順でlimit数だけ取得する */
    List<SubCategory> findFromById(
            @Param("id") Long id,
            @Param("limit") Integer limit);

    /** サブカテゴリIDから、同カテゴリ内表示降順でlimit数だけ取得する */
    List<SubCategory> findUpToById(
            @Param("id") Long id,
            @Param("limit") Integer limit);

    /** カテゴリIDに対応する「その他」サブカテゴリを取得する */
    Optional<SubCategory> findDefaultByCategoryId(@Param("categoryId") Long categoryId);

    /** カテゴリIDに対応する、サブカテゴリ名が既に存在するか確認する */
    boolean existsBySubCategoryNameAndCategoryId(
            @Param("name") String name,
            @Param("categoryId") Long categoryId);

    /** 追加 */
    void insert(SubCategory subCategory);

    /** 更新(名前のみ) */
    void update(SubCategory subCategory);

    /** 表示順を更新 */
    void updateDisplayOrder(@Param("id") Long id, @Param("displayOrder") Integer displayOrder);

    /** デフォルトフラグを更新 */
    void updateIsDefault(@Param("id") Long id, @Param("isDefault") Boolean isDefault);

    void bulkUpdateDisplayOrder(@Param("subCategories") List<SubCategory> subCategories);

    /** 削除 */
    void delete(@Param("id") Long id);

}
