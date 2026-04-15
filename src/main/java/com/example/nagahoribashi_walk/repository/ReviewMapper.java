package com.example.nagahoribashi_walk.repository;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.nagahoribashi_walk.dto.AdminReviewRow;
import com.example.nagahoribashi_walk.entity.Review;

/**
 * reviewsテーブルに対応したMapperのインターフェース
 * 
 */
@Mapper
public interface ReviewMapper {

    // 投稿
    void insert(Review review);

    // レビューIDで1件取得
    Optional<Review> findById(@Param("id") Long id);

    // 編集
    int update(Review review);

    // 削除
    void delete(@Param("userId") Long userId, @Param("spotId") Long spotId);

    /**
     * 指定したユーザーが指定したスポットにレビュー投稿済みか確認する
     *
     * @param userId ユーザーID
     * @param spotId スポットID
     * @return 投稿済みならtrue
     */
    boolean existsByUserIdAndSpotId(@Param("userId") Long userId, @Param("spotId") Long spotId);

    /** レビューの総数を取得する */
    long count();

    /** キーワードに対応したレビューをページネーション用に取得 */
    List<AdminReviewRow> findAllForAdminByKeyword(
            @Param("keyword") String keyword,
            @Param("offset") long offset,
            @Param("limit") int limit);

    /** キーワードに対応したレビューの総数を取得 */
    long countForAdminByKeyword(@Param("keyword") String keyword);

}
