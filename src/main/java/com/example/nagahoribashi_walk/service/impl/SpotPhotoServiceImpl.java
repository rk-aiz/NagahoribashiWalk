package com.example.nagahoribashi_walk.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagahoribashi_walk.entity.SpotPhoto;
import com.example.nagahoribashi_walk.repository.SpotPhotoMapper;
import com.example.nagahoribashi_walk.service.SpotPhotoService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SpotPhotoServiceImpl implements SpotPhotoService {

	private final SpotPhotoMapper spotPhotoMapper;
	
	/** 【管理者】スポットIDに対応する画像一覧を取得する */
	@Override
	public List<SpotPhoto> getAllBySpotId(Long spotId) {
		return spotPhotoMapper.findAllBySpotId(spotId);
		
	}
}
