package com.example.nagahoribashi_walk.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagahoribashi_walk.dto.FavoriteSummary;
import com.example.nagahoribashi_walk.repository.FavoriteMapper;
import com.example.nagahoribashi_walk.service.FavoriteService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteMapper favoriteMapper;

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
     * お気に入り登録
     */
    @Override
    public void addFavorite(Long userId, Long spotId) {

        // すでに登録されているか確認
        if (!favoriteMapper.existsByUserAndSpot(userId, spotId)) {
            favoriteMapper.insertFavorite(userId, spotId);
        }
    }

    @Override
    public boolean isFavorite(Long userId, Long spotId) {

        return favoriteMapper.existsByUserAndSpot(userId, spotId);
    }

    /**
     * お気に入り解除
     */
    @Override
    public void removeFavorite(Long userId, Long spotId) {

        // 削除
        favoriteMapper.deleteFavorite(userId, spotId);
    }

}
