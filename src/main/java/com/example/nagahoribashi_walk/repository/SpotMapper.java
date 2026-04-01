package com.example.nagahoribashi_walk.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.nagahoribashi_walk.dto.SpotSummary;

/**
 * spotsテーブルに対応したMapperのインターフェース
 * 
 * @author 海津
 */
@Mapper
public interface SpotMapper {

    /** ページネーション付きでスポット一覧を取得する */
    List<SpotSummary> findAll(@Param("offset") long offset, @Param("limit") int limit);

    List<SpotSummary> findRecommendedSpots();
    // findById

    // findByCategoryId
    List<SpotSummary> findByCategoryId(
    		@Param("categoryId") Long categoryId, 
    		@Param("offset") long offset, 
    		@Param("limit") int limit);

    // findBySubCategoryId
    List<SpotSummary> findBySubCategoryId(
    		@Param("subCategoryId") Long subCategoryId, 
    		@Param("offset") long offset, 
    		@Param("limit") int limit);

    List<SpotSummary> searchByKeywords(
    		@Param("keyword") String keyword,
    		@Param("offset") long offset,
    		@Param("limit") int limit);

    // findImagesBySpotId

    // findAllForAdmin

    // insert

    // update

    // softDelete

    // insertImage

    // deleteImage

    /** スポット数をカウントする */
    long count();
    
    long countByKeywords(@Param("keyword") String keyword);
    
    long countByCategoryId(@Param("categoryId") Long categoryId);
    
    long countBySubCategoryId(@Param("subCategoryId") Long subCategoryId);
}
