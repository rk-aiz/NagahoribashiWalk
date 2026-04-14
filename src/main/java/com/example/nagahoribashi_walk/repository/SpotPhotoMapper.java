package com.example.nagahoribashi_walk.repository;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.nagahoribashi_walk.entity.SpotPhoto;

/**
 * spot_photosテーブルに対応したMapperのインターフェース
 * 
 * @author 海津
 */
@Mapper
public interface SpotPhotoMapper {

	/** スポットIDから、紐づいた画像情報を全件取得 */
	List<SpotPhoto> findAllBySpotId(@Param("spotId") Long spotId);

	/** 指定されたスポットに紐づいた、指定されたdisplay_orderの画像情報を取得 */
	Optional<SpotPhoto> findBySpotIdAndDisplayOrder(@Param("spotId") Long spotId,
			@Param("displayOrder") Integer displayOrder);

	/** 指定されたスポットに紐づいた、指定されたdisplay_order「以上」の画像情報を全件取得 */
	List<SpotPhoto> findBySpotIdAndDisplayOrderGreaterThanEqual(@Param("spotId") Long spotId,
			@Param("displayOrder") Integer displayOrder);

	/** 指定されたIDから画像情報を取得 */
	Optional<SpotPhoto> findEntityById(@Param("id") Long id);

	/** 指定された画像URLのレコードがあるか確認 */
	boolean existsByPhotoUrl(@Param("photoUrl") String photoUrl);

	/** display_orderを一括更新 */
	void bulkUpdateDisplayOrder(@Param("spotPhotos") List<SpotPhoto> spotPhotos);

	// 画像情報追加
	void insert(SpotPhoto spotPhoto);

	// 画像情報削除
	void delete(@Param("id") Long id);

}
