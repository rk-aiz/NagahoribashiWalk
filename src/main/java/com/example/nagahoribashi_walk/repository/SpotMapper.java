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

        /** IDからスポット詳細情報を取得 */
        Optional<SpotDetail> findById(@Param("id") Long id);

        /** スポット一覧を取得する(ページネーション) */
        List<SpotSummary> findAll(@Param("offset") long offset, @Param("limit") int limit);

        /** カテゴリIDから、List<SpotSummary>を取得 */
        List<SpotSummary> findByCategoryId(
                        @Param("categoryId") Long categoryId,
                        @Param("offset") long offset,
                        @Param("limit") int limit);

        /** サブカテゴリIDから、List<SpotSummary>を取得 */
        List<SpotSummary> findBySubCategoryId(
                        @Param("subCategoryId") Long subCategoryId,
                        @Param("offset") long offset,
                        @Param("limit") int limit);

        /** キーワードで、スポット要約を取得する */
        List<SpotSummary> searchByKeywords(
                        @Param("keywordMatrix") List<List<String>> keywordMatrix,
                        @Param("offset") long offset,
                        @Param("limit") int limit);

        /** 関連スポット用 */
        List<SpotSummary> findRandomByAnyKeywords(
                        @Param("spotId") Long spotId,
                        @Param("keywords") List<String> keywords,
                        @Param("limit") int limit);

        /** 評価平均値降順でlimit数スポットを取得 */
        List<SpotSummary> findTopByRating(@Param("limit") int limit);

        /** お気に入り数降順でlimit数スポットを取得 */
        List<SpotSummary> findTopByFavorite(@Param("limit") int limit);

        /** ランダムでlimit数スポットを取得 */
        List<SpotSummary> findRandomSpots(@Param("limit") int limit);

        /** IDからスポット要約情報を取得 */
        Optional<SpotSummary> findSummaryById(@Param("id") Long id);

        /** スポット数をカウントする */
        long count();

        /** キーワードに対応したスポットの総数を取得 */
        long countByKeywords(@Param("keywordMatrix") List<List<String>> keywordMatrix);

        /** カテゴリIDに対応したスポットの総数を取得 */
        long countByCategoryId(@Param("categoryId") Long categoryId);

        /** サブカテゴリIDに対応したスポットの総数を取得 */
        long countBySubCategoryId(@Param("subCategoryId") Long subCategoryId);

        /** IDに対応するスポットが存在するか確認 */
        boolean existsBySpotId(@Param("spotId") Long spotId);

        /** PV数を1加算 */
        void incrementPvCount(@Param("id") Long id);
}
