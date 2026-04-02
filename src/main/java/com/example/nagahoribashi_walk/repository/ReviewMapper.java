package com.example.nagahoribashi_walk.repository;

import org.apache.ibatis.annotations.Mapper;

import com.example.nagahoribashi_walk.entity.Review;

/**
 * reviewsテーブルに対応したMapperのインターフェース
 * 
 */
@Mapper
public interface ReviewMapper {

	 //findBySpotId(Long spotId);
	
	//投稿
	void insert(Review review);
	
	//編集
//	Long update(Review review);
	
	//削除
//	void deletet(Long userId, Long SpotId);
	
	//存在確認
	//boolean existsBySpotIdAndUserId(Long userId, Long spotId);

}
