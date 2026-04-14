package com.example.nagahoribashi_walk.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagahoribashi_walk.dto.AdminReviewRow;
import com.example.nagahoribashi_walk.entity.Review;
import com.example.nagahoribashi_walk.exception.ReviewAlreadyExistsException;
import com.example.nagahoribashi_walk.exception.ReviewOperationException;
import com.example.nagahoribashi_walk.repository.ReviewMapper;
import com.example.nagahoribashi_walk.repository.SpotMapper;
import com.example.nagahoribashi_walk.repository.UserMapper;
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

    /** 投稿時に付与するポイント数 */
    @Value("${review.post-point}")
    private int reviewPostPoint;

    /** 投稿からポイントを減算するウィンドウ時間（時間単位） */
    @Value("${review.delete-penalty-window-hours}")
    private int deletePenaltyWindowHours;

    private final ReviewMapper reviewMapper;
    private final SpotMapper spotMapper;
    private final UserMapper userMapper;

    /** レビューを投稿する。投稿に成功した場合は付与したポイント数を返す。 */
    @Override
    public int addReview(Review review, Long userId) {

        // 1. スポットが存在するか確認
        if (!spotMapper.existsBySpotId(review.getSpotId())) {
            throw new ReviewOperationException("対象のスポットが存在しません。", review.getSpotId());
        }

        // 2. 同じスポットに対して同じユーザーが既に投稿している場合は投稿不可
        if (reviewMapper.existsByUserIdAndSpotId(userId, review.getSpotId())) {
            throw new ReviewAlreadyExistsException("このスポットには既にレビューを投稿しています。", review.getSpotId(), userId);
        }

        // 3. 投稿者のユーザーIDをセット
        review.setUserId(userId);

        // 4. 保存処理を実行
        reviewMapper.insert(review);

        // 5. ポイント付与
        userMapper.addPoint(userId, reviewPostPoint);
        return reviewPostPoint;
    }

    /**
     * レビューIDに対応するレビューを1件取得する
     * 
     * @param reviewId レビューID
     * @return レビュー情報
     */
    @Override
    public Review getById(Long reviewId) {
        return reviewMapper.findById(reviewId).orElseThrow();
    }

    /** レビュー情報を更新する */
    @Override
    public void updateReview(Review review, Long userId) {

        // 更新対象のレビューを取得
        Review existingReview = reviewMapper.findById(review.getId()).orElseThrow();

        // スポットが存在するか確認
        if (!spotMapper.existsBySpotId(existingReview.getSpotId())) {
            throw new ReviewOperationException("対象のスポットが存在しません。", existingReview.getSpotId());
        }

        // 自分のレビュー以外は更新させない
        if (!existingReview.getUserId().equals(userId)) {
            throw new ReviewOperationException("他のユーザーのレビューは更新できません。", existingReview.getSpotId());
        }

        // 更新条件に必要な userId をセット
        review.setUserId(userId);

        // spotId も update 条件に必要なので元データから引き継ぐ
        review.setSpotId(existingReview.getSpotId());

        // 更新処理を実行
        reviewMapper.update(review);
    }

    /** レビューを削除する。投稿から一定時間以内の削除はポイントを減算し、その差分を返す。 */
    @Override
    public int deleteReview(Long reviewId, Long userId) {

        // 削除対象のレビューを取得
        Review existingReview = reviewMapper.findById(reviewId).orElseThrow();

        // 自分のレビュー以外は削除させない
        if (!existingReview.getUserId().equals(userId)) {
            throw new ReviewOperationException("他のユーザーのレビューは削除できません。", existingReview.getSpotId());
        }

        // userId と spotId を条件に削除を実行
        reviewMapper.delete(userId, existingReview.getSpotId());

        // 投稿からウィンドウ時間以内の削除はポイントを減算する
        if (existingReview.getCreatedAt() != null
                && existingReview.getCreatedAt().isAfter(LocalDateTime.now().minusHours(deletePenaltyWindowHours))) {
            userMapper.addPoint(userId, -reviewPostPoint);
            return -reviewPostPoint;
        }
        return 0;
    }

    /** 【管理者】レビュー一覧をページネーションで取得 */
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

    /** 【管理者】全体のレビュー数を取得する */
    @Override
    public long getReviewCount() {
        return reviewMapper.count();
    }
}
