package com.example.nagahoribashi_walk.service;

import com.example.nagahoribashi_walk.entity.Review;

public interface ReviewService {

	/**
	 * レビュー情報を登録する
	 * 
	 * @param review レビュー情報
	 * @param username ログイン中のユーザー名
	 */
	void addReview(Review review, Long userId);
}
