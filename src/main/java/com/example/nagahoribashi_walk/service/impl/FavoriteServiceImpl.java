package com.example.nagahoribashi_walk.service.impl;

import org.springframework.data.domain.Page;
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
		// TODO 自動生成されたメソッド・スタブ
		return null;
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

	/**
	 * お気に入り解除
	 */
	@Override
	public void removeFavorite(Long userId, Long spotId) {

		// 削除
		favoriteMapper.deleteFavorite(userId, spotId);
	}

}
