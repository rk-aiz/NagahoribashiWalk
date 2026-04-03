package com.example.nagahoribashi_walk.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagahoribashi_walk.entity.Review;
import com.example.nagahoribashi_walk.repository.ReviewMapper;
import com.example.nagahoribashi_walk.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

	private final ReviewMapper reviewMapper;

	/**
	 * レビューを投稿する
	 * 
	 *@Param review レビュー情報
	 */
	@Override
	public void addReview(Review review) {
		
		// 投稿していいか条件確認
		
		// 投稿できない場合↓
		//throw new IllegalArgumentException("[投稿できない理由]のため投稿できません");
		
		reviewMapper.insert(review);
	}

}
