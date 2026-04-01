package com.example.nagahoribashi_walk.service;

public interface FavoriteService {
	
       //お気に入り登録
	    void addFavorite(Long userId, Long spotId);
	    
	   //お気に入り削除
	    void removeFavorite(Long userId, Long spotId);
	}

