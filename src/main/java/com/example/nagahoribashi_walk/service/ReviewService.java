package com.example.nagahoribashi_walk.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.nagahoribashi_walk.dto.AdminReviewRow;
import com.example.nagahoribashi_walk.entity.Review;

public interface ReviewService {

    /**
     * レビュー情報を登録する。投稿に成功した場合は付与したポイント数を返す。
     *
     * @param review レビュー情報
     * @param userId ログイン中のユーザーID
     * @return 付与したポイント数
     */
    int addReview(Review review, Long userId);

    /**
     * レビューIDに対応するレビューを1件取得する
     * 
     * @param reviewId レビューID
     * @return レビュー情報
     */
    Review getById(Long reviewId);

    /**
     * レビュー情報を更新する
     * 
     * @param review レビュー情報
     * @param userId ログイン中のユーザーID
     */
    void updateReview(Review review, Long userId);

    /**
     * レビューを削除する。投稿から一定時間以内の削除はポイントを減算し、その差分を返す。
     *
     * @param reviewId レビューID
     * @param userId   ログイン中のユーザーID
     * @return ポイント差分（減算なしは0）
     */
    int deleteReview(Long reviewId, Long userId);

    /** 【管理者】レビューを削除する */
    void deleteForAdmin(Long reviewId);

    /** 【管理者】レビュー一覧をページネーションで取得 */
    Page<AdminReviewRow> getAdminReviewPage(Pageable pageable, String keyword);

    /** 全体のレビュー数を取得する TODO : 退会済みユーザーのレビューを除くか検討 */
    long getReviewCount();

}
