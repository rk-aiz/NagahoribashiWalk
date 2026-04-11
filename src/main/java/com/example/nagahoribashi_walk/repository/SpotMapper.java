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
 * @author 海津
 */
@Mapper
public interface SpotMapper {

    /** スポット一覧を取得する(ページネーション) */
    List<SpotSummary> findAll(@Param("offset") long offset, @Param("limit") int limit);

    // findById
    Optional<SpotDetail> findById(@Param("id") Long id);

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
    List<SpotSummary> findRandomByAnyKeyword(
            @Param("spotId") Long spotId,
            @Param("keywords") List<String> keywords,
            @Param("limit") int limit);

    boolean existsBySpotId(@Param("spotId") Long spotId);

    /** スポット数をカウントする */
    long count();

    long countByKeywords(@Param("keywordMatrix") List<List<String>> keywordMatrix);

    long countByCategoryId(@Param("categoryId") Long categoryId);

    long countBySubCategoryId(@Param("subCategoryId") Long subCategoryId);

    List<SpotSummary> findTopByRating();

    List<SpotSummary> findTopByFavorite();

    List<SpotSummary> findRandomSpots();

    // PV数を1加算
    void incrementPvCount(@Param("id") Long id);
}
