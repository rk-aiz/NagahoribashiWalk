package com.example.nagahoribashi_walk.repository;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.nagahoribashi_walk.dto.SpotDetail;
import com.example.nagahoribashi_walk.dto.SpotSummary;
import com.example.nagahoribashi_walk.entity.Spot;

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
    Optional<SpotDetail> findById(@Param("id") Long id);

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
    		@Param("hira") String hira,
    		@Param("kana") String kana,
    		@Param("offset") long offset,
    		@Param("limit") int limit);

    // findImagesBySpotId

    // findAllForAdmin

     void insert(Spot spot);
    
     void update(Spot spot);

    // softDelete

    // insertImage

    // deleteImage

    /** スポット数をカウントする */
    long count();
    
    long countByCategoryId(@Param("categoryId") Long categoryId);
    
    long countBySubCategoryId(@Param("subCategoryId") Long subCategoryId);

    long countByKeywords(@Param("hira") String hira, @Param("kana") String kana);

}
