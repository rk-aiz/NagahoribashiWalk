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
	public void addReview(Review review,String username) {

		// ユーザー名からユーザーIDを取得
		Long userId = reviewMapper.findUserIdByUsername(username);

		// ユーザーIDが取得できない場合は投稿不可
		if(userId == null) {
			throw new IllegalArgumentException("[投稿できない理由]のため投稿できません");
		}

		//投稿者のユーザーIDをセット
		review.setUserId(userId);

		//同じスポットに対して同じユーザーが既に投稿している場合は投稿不可
		if(reviewMapper.existsByUserIdAndSpotId(userId,review.getSpotId())) {
			throw new IllegalArgumentException("このスポットには既にレビューを投稿しています。");
		}

		//投稿者のユーザーIDをセット
		reviewMapper.insert(review);
	}

}
