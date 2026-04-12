package com.example.nagahoribashi_walk.repository;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.nagahoribashi_walk.dto.SpotDetail;
import com.example.nagahoribashi_walk.dto.SpotSummary;

/**
 * spotsテーブルに対応したMapperのインターフェース(一般・ユーザー側)
 * 
 * @author 海津、篠原、池田、大谷
 */
@Mapper
public interface SpotMapper {

    // findById
    Optional<SpotDetail> findById(@Param("id") Long id);

    /** スポット一覧を取得する(ページネーション) */
    List<SpotSummary> findAll(@Param("offset") long offset, @Param("limit") int limit);

    List<SpotSummary> findByCategoryId(
            @Param("categoryId") Long categoryId,
            @Param("offset") long offset,
            @Param("limit") int limit);

    List<SpotSummary> findBySubCategoryId(
            @Param("subCategoryId") Long subCategoryId,
            @Param("offset") long offset,
            @Param("limit") int limit);

    List<SpotSummary> searchByKeywords(
            @Param("keywordMatrix") List<List<String>> keywordMatrix,
            @Param("offset") long offset,
            @Param("limit") int limit);

    // 関連スポット用
    List<SpotSummary> findRandomByAnyKeywords(
            @Param("spotId") Long spotId,
            @Param("keywords") List<String> keywords,
            @Param("limit") int limit);

    List<SpotSummary> findTopByRating(@Param("limit") int limit);

    List<SpotSummary> findTopByFavorite(@Param("limit") int limit);

    List<SpotSummary> findRandomSpots(@Param("limit") int limit);

    Optional<SpotSummary> findSummaryById(@Param("id") Long id);

    /** スポット数をカウントする */
    long count();

    long countByKeywords(@Param("keywordMatrix") List<List<String>> keywordMatrix);

    long countByCategoryId(@Param("categoryId") Long categoryId);

    long countBySubCategoryId(@Param("subCategoryId") Long subCategoryId);

    boolean existsBySpotId(@Param("spotId") Long spotId);

    // PV数を1加算
    void incrementPvCount(@Param("id") Long id);
}
