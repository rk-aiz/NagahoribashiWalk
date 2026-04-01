package com.example.nagahoribashi_walk.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.nagahoribashi_walk.dto.FavoriteSummary;

/**
 * favoritesテーブルに対応したMapperのインターフェース
 * 
 */
@Mapper
public interface FavoriteMapper {

	//お気に入り登録削除（正本）

	// ① お気に入り追加（INSERT）
	void insertFavorite(@Param("userId") Long userId,
			@Param("spotId") Long spotId);

	// ② お気に入り削除（DELETE）
	void deleteFavorite(@Param("userId") Long userId,
			@Param("spotId") Long spotId);

	// ③ お気に入り存在確認（SELECT EXISTS）
	boolean existsByUserAndSpot(@Param("userId") Long userId,
			@Param("spotId") Long spotId);

	
	/** マイページ用にお気に入り一覧をページネーションありで取得 */
	List<FavoriteSummary> findByUserId(
			@Param("userId") Long userId, 
			@Param("offset") long offset, 
			@Param("limit") int limit);
	
	long countByUserId(@Param("userId") Long userId) ;

}
