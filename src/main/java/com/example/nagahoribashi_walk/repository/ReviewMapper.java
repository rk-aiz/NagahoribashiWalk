package com.example.nagahoribashi_walk.repository;

import org.apache.ibatis.annotations.Mapper;

import com.example.nagahoribashi_walk.entity.Review;

/**
 * reviewsテーブルに対応したMapperのインターフェース
 * 
 */
@Mapper
public interface ReviewMapper {
	
	//投稿
	void insert(Review review);
}
