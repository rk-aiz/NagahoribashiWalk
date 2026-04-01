package com.example.nagahoribashi_walk.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagahoribashi_walk.repository.FavoriteMapper;
import com.example.nagahoribashi_walk.service.FavoriteService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteMapper favoriteMapper;
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

        /**
         * お気に入り解除
         */
        @Override
        public void removeFavorite(Long userId, Long spotId) {

            // 削除
            favoriteMapper.deleteFavorite(userId, spotId);
        }
        
        @Override
        public boolean existsByUserAndSpot(Long userId, Long spotId) {
        	
        	return favoriteMapper.existsByUserAndSpot(userId, spotId);
        }
    }

