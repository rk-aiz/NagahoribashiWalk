package com.example.nagahoribashi_walk.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.nagahoribashi_walk.dto.SaveImagesResult;
import com.example.nagahoribashi_walk.entity.SpotPhoto;

public interface SpotPhotoService {

	/** 【管理者】スポットIDに対応する画像一覧を取得する */
	List<SpotPhoto> getAllBySpotId(Long spotId);

	SaveImagesResult saveImages(List<MultipartFile> files, Long spotId, Integer firstDisplayOrder);

	void delete(Long id, Long spotId);

    void reorder(Long spotId, Integer displayOrder1, Integer displayOrder2);

}
