package com.example.nagahoribashi_walk.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.nagahoribashi_walk.dto.SaveImagesResult;
import com.example.nagahoribashi_walk.entity.SpotPhoto;
import com.example.nagahoribashi_walk.repository.SpotPhotoMapper;
import com.example.nagahoribashi_walk.service.FileStorageService;
import com.example.nagahoribashi_walk.service.SpotPhotoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * スポット画像関連サービスの実装
 *
 * @author 海津
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SpotPhotoServiceImpl implements SpotPhotoService {

    private final SpotPhotoMapper spotPhotoMapper;
    private final FileStorageService fileStorageService;

    /** 【管理者】スポットIDに対応する画像一覧を取得する */
    @Override
    public List<SpotPhoto> getAllBySpotId(Long spotId) {
        return spotPhotoMapper.findAllBySpotId(spotId);
    }

    /** スポットIDに対応する画像の表示順を入れ替える */
    @Override
    public void reorder(Long spotId, Integer displayOrder1, Integer displayOrder2) {
        SpotPhoto spotPhoto1 = spotPhotoMapper.findBySpotIdAndDisplayOrder(spotId, displayOrder1).orElseThrow();
        SpotPhoto spotPhoto2 = spotPhotoMapper.findBySpotIdAndDisplayOrder(spotId, displayOrder2).orElseThrow();
        spotPhoto1.setDisplayOrder(displayOrder2);
        spotPhoto2.setDisplayOrder(displayOrder1);
        spotPhotoMapper.bulkUpdateDisplayOrder(List.of(spotPhoto1, spotPhoto2));
    }

    /** 【管理者】画像ファイル一覧を保存する */
    @Override
    public SaveImagesResult saveImages(List<MultipartFile> files, Long spotId, Integer firstDisplayOrder) {
        List<String> savedPhotoUrls = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        // 1: ファイルを保存し、相対パス（例: images/uuid.jpg）を収集する
        for (MultipartFile file : files) {
            try {
                String photoUrl = fileStorageService.saveImage(file);
                if (photoUrl == null) {
                    errors.add(file.getOriginalFilename() + ": スキップ");
                    continue;
                }
                savedPhotoUrls.add(photoUrl);
            } catch (IOException e) {
                errors.add(file.getOriginalFilename() + ": 保存失敗");
            }
        }

        // 2: firstDisplayOrder を有効範囲にクランプ
        int maxOrder = spotPhotoMapper.findMaxDisplayOrderBySpotId(spotId);
        int insertAt = Math.min(firstDisplayOrder, maxOrder + 1);

        // 3: 既存レコードのリオーダー
        int offset = savedPhotoUrls.size();
        List<SpotPhoto> existingPhotos = spotPhotoMapper.findBySpotIdAndDisplayOrderGreaterThanEqual(spotId, insertAt);
        if (!existingPhotos.isEmpty()) {
            for (SpotPhoto sp : existingPhotos) {
                sp.setDisplayOrder(sp.getDisplayOrder() + offset);
            }
            spotPhotoMapper.bulkUpdateDisplayOrder(existingPhotos);
        }

        // 4: 新規レコードのInsert
        int displayOrder = insertAt;
        for (String photoUrl : savedPhotoUrls) {
            spotPhotoMapper.insert(SpotPhoto.builder()
                    .spotId(spotId)
                    .displayOrder(displayOrder++)
                    .photoUrl(photoUrl)
                    .build());
        }

        return new SaveImagesResult(savedPhotoUrls, errors);
    }

    /**
     * 画像を削除する
     */
    @Override
    public void delete(Long id, Long spotId) {

        // ファイル情報を取得
        SpotPhoto spotPhoto = spotPhotoMapper.findEntityById(id).orElseThrow();

        // ファイル情報をDBから削除
        spotPhotoMapper.delete(id);

        // 表示順を正規化する
        normalizeDisplayOrder(spotId);

        // まだ同じファイルを参照しているレコードがある場合はreturn
        if (spotPhotoMapper.existsByPhotoUrl(spotPhoto.getPhotoUrl())) {
            return;
        }

        fileStorageService.deleteImage(spotPhoto.getPhotoUrl());
    }

    /**
     * 表示順を正規化する
     */
    private void normalizeDisplayOrder(Long spotId) {
        List<SpotPhoto> photos = spotPhotoMapper.findAllBySpotId(spotId);
        boolean requireUpdate = false;
        if (!photos.isEmpty()) {
            for (int i = 0; i < photos.size(); i++) {
                SpotPhoto sp = photos.get(i);
                if (!sp.getDisplayOrder().equals(i + 1)) {
                    sp.setDisplayOrder(i + 1);
                    requireUpdate = true;
                }
            }
        }
        if (requireUpdate) {
            spotPhotoMapper.bulkUpdateDisplayOrder(photos);
        }
    }
}
