package com.example.nagahoribashi_walk.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
        
        long count();

    }

