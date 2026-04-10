package com.example.nagahoribashi_walk.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.nagahoribashi_walk.dto.AdminSpotRow;
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
    
    //PV数を1加算
    void incrementPvCount(@Param("id")Long id);

    List<SpotSummary> findByCategoryId(
            @Param("categoryId") Long categoryId,
            @Param("offset") long offset,
            @Param("limit") int limit);

    List<SpotSummary> findBySubCategoryId(
            @Param("subCategoryId") Long subCategoryId,
            @Param("offset") long offset,
            @Param("limit") int limit);

    List<SpotSummary> searchByKeywords(
            @Param("keywordList") List<Map<String, String>> keywordList,
            @Param("offset") long offset,
            @Param("limit") int limit);

    boolean existsBySpotId(@Param("spotId") Long spotId);

    /** スポット数をカウントする */
    long count();

    long countByKeywords(@Param("keywordList") List<Map<String, String>> keywordList);

    long countByCategoryId(@Param("categoryId") Long categoryId);

    long countBySubCategoryId(@Param("subCategoryId") Long subCategoryId);

    List<SpotSummary> findTopByRating();

    List<SpotSummary> findTopByFavorite();

    List<SpotSummary> findRandomSpots();
}
