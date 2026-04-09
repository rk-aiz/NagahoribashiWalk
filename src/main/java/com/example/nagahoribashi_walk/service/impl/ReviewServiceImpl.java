package com.example.nagahoribashi_walk.service.impl;

import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagahoribashi_walk.dto.AdminReviewRow;
import com.example.nagahoribashi_walk.entity.Review;
import com.example.nagahoribashi_walk.exception.ReviewAlreadyExistsException;
import com.example.nagahoribashi_walk.repository.ReviewMapper;
import com.example.nagahoribashi_walk.service.ReviewService;

import lombok.RequiredArgsConstructor;

/**
 * レビュー関連のサービス
 *
 * @author 池田
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewMapper reviewMapper;

    /**
     * レビューを投稿する
     * 
     * @Param review レビュー情報
     */
    @Override
    public void addReview(Review review, Long userId) {

        // 同じスポットに対して同じユーザーが既に投稿している場合は投稿不可
        if (reviewMapper.existsByUserIdAndSpotId(userId, review.getSpotId())) {
            throw new ReviewAlreadyExistsException("このスポットには既にレビューを投稿しています。");
        }

        // 投稿者のユーザーIDをセット
        review.setUserId(userId);

        // 保存処理を実行
        reviewMapper.insert(review);
    }

    /**
     * レビューIDに対応するレビューを1件取得する
     * 
     * @param reviewId レビューID
     * @return レビュー情報
     */
    @Override
    public Review findById(Long reviewId) {
        return reviewMapper.findById(reviewId).orElseThrow();
    }

    /**
     * レビュー情報を更新する
     * 
     * @param review レビュー情報
     * @param userId ログイン中のユーザーID
     */
    @Override
    public void updateReview(Review review, Long userId) {

        // 更新対象のレビューを取得
        Review existingReview = reviewMapper.findById(review.getId()).orElseThrow();

        // 自分のレビュー以外は更新させない
        if (!existingReview.getUserId().equals(userId)) {
            throw new IllegalArgumentException("他のユーザーのレビューは更新できません。");
        }

        // 更新条件に必要な userId をセット
        review.setUserId(userId);

        // spotId も update 条件に必要なので元データから引き継ぐ
        review.setSpotId(existingReview.getSpotId());

        // 更新処理を実行
        reviewMapper.update(review);
    }

    /**
     * レビューを削除する
     * 
     * @param reviewId レビューID
     * @param userId   ログイン中のユーザーID
     */
    @Override
    public void deleteReview(Long reviewId, Long userId) {

        // 削除対象のレビューを取得
        Review existingReview = reviewMapper.findById(reviewId).orElseThrow();

        // 自分のレビュー以外は削除させない
        if (!existingReview.getUserId().equals(userId)) {
            throw new IllegalArgumentException("他のユーザーのレビューは削除できません。");
        }

        // userId と spotId を条件に削除を実行
        reviewMapper.delete(userId, existingReview.getSpotId());
    }

    /**  */
    @Override
    public Page<AdminReviewRow> getAdminReviewPage(Pageable pageable, String keyword) {

        long total = reviewMapper.countForAdminByKeyword(keyword);
        List<AdminReviewRow> reviews = reviewMapper.findAllForAdminByKeyword(
                keyword, pageable.getOffset(), pageable.getPageSize());

        return new PageImpl<>(reviews, pageable, total);
    }

    /** 【管理者】レビューを削除する */
    @Override
    public void deleteForAdmin(Long reviewId) {

        // 削除対象のレビューを取得
        Review existingReview = reviewMapper.findById(reviewId).orElseThrow();

        // userId と spotId を条件に削除を実行
        reviewMapper.delete(existingReview.getUserId(), existingReview.getSpotId());
    }

    @Override
    public long getReviewCount() {
        return reviewMapper.count();
    }
}
