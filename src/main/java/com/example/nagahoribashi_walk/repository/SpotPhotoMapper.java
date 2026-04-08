package com.example.nagahoribashi_walk.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.nagahoribashi_walk.entity.SpotPhoto;

/**
 * reviewsテーブルに対応したMapperのインターフェース
 * 
 */
@Mapper
public interface SpotPhotoMapper {

	/** スポットIDから、紐づいた画像情報を全件取得 */
	List<SpotPhoto> findAllBySpotId(@Param("spotId") Long spotId);
	
	//画像情報追加
	void savePhoto(SpotPhoto spotPhoto);
	
	//画像情報削除
	void delete(@Param("id") Long id);

}
