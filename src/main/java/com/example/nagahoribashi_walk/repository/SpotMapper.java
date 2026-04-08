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
            @Param("keywordList") List<Map<String, String>> keywordList,
            @Param("offset") long offset,
            @Param("limit") int limit);

    // findImagesBySpotId

    List<AdminSpotRow> findAllForAdmin(
    		@Param("offset") long offset, @Param("limit") int limit);

    List<AdminSpotRow> findAllForAdminByKeyword(
            @Param("keyword") String keyword,
            @Param("offset") long offset,
            @Param("limit") int limit);

    long countForAdminByKeyword(@Param("keyword") String keyword);

    void insert(Spot spot);

    void update(Spot spot);

    Optional<Spot> findByIdForAdmin(@Param("spotId") Long spotId);
    
    // softDelete

    // insertImage

    // deleteImage

    /** スポット数をカウントする */
    long count();

    /** 全スポットの平均評価を取得する（小数第1位） */
    Double findAverageRatingAll();

    /** 最近登録されたスポットをN件取得する */
    List<AdminSpotRow> findRecent(@Param("limit") int limit);

    long countByKeywords(@Param("keywordList") List<Map<String, String>> keywordList);

    long countByCategoryId(@Param("categoryId") Long categoryId);

    long countBySubCategoryId(@Param("subCategoryId") Long subCategoryId);

}
