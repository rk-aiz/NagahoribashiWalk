package com.example.nagahoribashi_walk.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.nagahoribashi_walk.dto.FavoriteSummary;

public interface FavoriteService {
	
		Page<FavoriteSummary> getPage(Long userId, Pageable pageable);
	
       //お気に入り登録
	    void addFavorite(Long userId, Long spotId);
	    
	   //お気に入り削除
	    void removeFavorite(Long userId, Long spotId);
	    
	   //お気に入り存在確認
	    boolean isFavorite(Long userid, Long spotId);
	}

