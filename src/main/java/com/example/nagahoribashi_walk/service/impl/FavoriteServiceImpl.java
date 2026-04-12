package com.example.nagahoribashi_walk.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagahoribashi_walk.dto.FavoriteSummary;
import com.example.nagahoribashi_walk.entity.User;
import com.example.nagahoribashi_walk.repository.FavoriteMapper;
import com.example.nagahoribashi_walk.repository.UserMapper;
import com.example.nagahoribashi_walk.service.FavoriteService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;

@Service
@Transactional
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    @Value("${fortune.bonus-point}")
    private int fortuneBonusPoint;

    private final FavoriteMapper favoriteMapper;
    private final UserMapper userMapper;

    /**
     * お気に入り一覧をページネーションで取得
     */
    @Override
    public Page<FavoriteSummary> getPage(Long userId, Pageable pageable) {
        // お気に入りの総数を取得する
        long total = favoriteMapper.countByUserId(userId);

        // 対象ページに対応したスポットを取得する
        List<FavoriteSummary> spots = favoriteMapper.findByUserId(
                userId, pageable.getOffset(), pageable.getPageSize());

        // Page<T>インスタンスに詰めて返す
        return new PageImpl<>(spots, pageable, total);
    }

    /**
     * お気に入り登録。
     * 新規追加かつおすすめスポットの場合はボーナスポイントを付与し、その差分を返す。
     * ポイント変動なしは0を返す。
     */
    @Override
    public int addFavorite(Long userId, Long spotId) {

        boolean wasNew = !favoriteMapper.existsByUserAndSpot(userId, spotId);
        if (wasNew) {
            favoriteMapper.insertFavorite(userId, spotId);
        }

        // 新規追加かつおすすめスポットかつ24時間以内のおみくじならボーナス付与
        User user = userMapper.findById(userId).orElseThrow();
        if (wasNew && spotId.equals(user.getRecommendedSpotId()) && isWithinBonusWindow(user)) {
            userMapper.addPoint(userId, fortuneBonusPoint);
            return fortuneBonusPoint;
        }
        return 0;
    }

    @Override
    public boolean isFavorite(Long userId, Long spotId) {

        return favoriteMapper.existsByUserAndSpot(userId, spotId);
    }

    /**
     * お気に入り解除。
     * 登録済みかつおすすめスポットの場合はボーナスポイントを減算し、その差分を返す。
     * ポイント変動なしは0を返す。
     */
    @Override
    public int removeFavorite(Long userId, Long spotId) {

        boolean existed = favoriteMapper.existsByUserAndSpot(userId, spotId);
        favoriteMapper.deleteFavorite(userId, spotId);

        // 登録済みかつおすすめスポットかつ24時間以内のおみくじならポイント減算
        User user = userMapper.findById(userId).orElseThrow();
        if (existed && spotId.equals(user.getRecommendedSpotId()) && isWithinBonusWindow(user)) {
            userMapper.addPoint(userId, -fortuneBonusPoint);
            return -fortuneBonusPoint;
        }
        return 0;
    }

    /** おみくじを引いてから24時間以内かどうかを判定する */
    private boolean isWithinBonusWindow(User user) {
        return user.getLastDrawnAt() != null
                && user.getLastDrawnAt().isAfter(LocalDateTime.now().minusHours(24));
    }

}
