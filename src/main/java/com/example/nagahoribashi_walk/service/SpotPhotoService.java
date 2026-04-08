package com.example.nagahoribashi_walk.service;

import java.util.List;

import com.example.nagahoribashi_walk.entity.SpotPhoto;

public interface SpotPhotoService {

	/** 【管理者】スポットIDに対応する画像一覧を取得する */
	List<SpotPhoto> getAllBySpotId(Long spotId);

}
