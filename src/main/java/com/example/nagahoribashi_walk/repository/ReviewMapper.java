package com.example.nagahoribashi_walk.repository;

import org.apache.ibatis.annotations.Mapper;

import com.example.nagahoribashi_walk.entity.Review;

/**
 * reviewsテーブルに対応したMapperのインターフェース
 * 
 */
@Mapper
public interface ReviewMapper {

	//編集
	Long update(Review review);
	
	//削除
	void delete(Long userId, Long SpotId);
	
}
