package com.example.nagahoribashi_walk.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.nagahoribashi_walk.dto.SaveImagesResult;
import com.example.nagahoribashi_walk.entity.SpotPhoto;

/**
 * スポット画像管理サービスのインターフェース
 */
public interface SpotPhotoService {

	/** 【管理者】スポットIDに対応する画像一覧を取得する */
	List<SpotPhoto> getAllBySpotId(Long spotId);

	/** 【管理者】スポットIDに対応する画像ファイル一覧を保存する */
	SaveImagesResult saveImages(List<MultipartFile> files, Long spotId, Integer firstDisplayOrder);

	/** 【管理者】画像ファイル情報を削除する */
	void delete(Long id, Long spotId);

	/** 【管理者】画像情報の表示順を並べ替える */
	void reorder(Long spotId, Integer displayOrder1, Integer displayOrder2);

}
