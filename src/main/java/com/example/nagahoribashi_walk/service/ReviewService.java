package com.example.nagahoribashi_walk.service;

import com.example.nagahoribashi_walk.entity.Review;

public interface ReviewService {

	/**
	 * レビュー情報を登録する
	 * 
	 * @param review レビュー情報
	 */
	void addReview(Review review);
}
