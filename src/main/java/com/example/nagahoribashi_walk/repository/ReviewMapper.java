package com.example.nagahoribashi_walk.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.nagahoribashi_walk.entity.Review;

/**
 * reviewsテーブルに対応したMapperのインターフェース
 * 
 */
@Mapper
public interface ReviewMapper {

	//投稿
	void insert(Review review);

	//編集
	int update(Review review);

	//削除
	void delete(@Param("userId") Long userId, @Param("spotId") Long spotId);
	
	/**
	 * 指定したユーザーが指定したスポットにレビュー投稿済みか確認する
	 *
	 * @param userId ユーザーID
	 * @param spotId スポットID
	 * @return 投稿済みならtrue
	 */
	boolean existsByUserIdAndSpotId(@Param("userId") Long userId, @Param("spotId") Long spotId);

	/** レビューの総数を取得する */
	long count();

}
