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
	


	//指定されたスポットに紐づいた、指定されたdisplay_order以上の画像情報を全件取得
	List<SpotPhoto> findBySpotIdAndDisplayOrderGreaterThanEqual(@Param("spotId") Long spotId,
			@Param("displayOrder") Integer displayOrder);

	void bulkUpdateDisplayOrder(@Param("spotPhotos") List<SpotPhoto> spotPhotos);

	//画像情報追加
	void insert(SpotPhoto spotPhoto);

	//画像情報削除
	void delete(@Param("id") Long id);


}
