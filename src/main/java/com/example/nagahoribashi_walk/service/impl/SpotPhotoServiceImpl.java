package com.example.nagahoribashi_walk.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.nagahoribashi_walk.dto.SaveImagesResult;
import com.example.nagahoribashi_walk.entity.SpotPhoto;
import com.example.nagahoribashi_walk.repository.SpotPhotoMapper;
import com.example.nagahoribashi_walk.service.SpotPhotoService;
import com.example.nagahoribashi_walk.util.MyStringUtils;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SpotPhotoServiceImpl implements SpotPhotoService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    private String imagePrefix = "images";

    private final SpotPhotoMapper spotPhotoMapper;

    /** 【管理者】スポットIDに対応する画像一覧を取得する */
    @Override
    public List<SpotPhoto> getAllBySpotId(Long spotId) {
        return spotPhotoMapper.findAllBySpotId(spotId);
    }

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
        List<String> savedFilenames = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        // 保存先ディレクトリを作成（存在しない場合）
        Path uploadPath = Paths.get(MyStringUtils.joinPath(uploadDir, imagePrefix));
        try {
            Files.createDirectories(uploadPath);
        } catch (IOException e) {
            throw new RuntimeException("アップロードディレクトリの作成に失敗しました", e);
        }

        for (MultipartFile file : files) {
            if (file.isEmpty() || (!Optional.ofNullable(file.getContentType()).orElse("").startsWith("image/"))) {
                errors.add(file.getOriginalFilename() + ": スキップ");
                continue;
            }
            try {
                // ファイル名の重複を避けるためUUIDを使用
                String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
                String newFilename = UUID.randomUUID() + "." + extension;

                // ファイルを保存
                Files.copy(file.getInputStream(), uploadPath.resolve(newFilename), StandardCopyOption.REPLACE_EXISTING);

                savedFilenames.add(newFilename);
            } catch (IOException e) {
                errors.add(file.getOriginalFilename() + ": 保存失敗");
            }
        }

        // 2: 既存レコードのリオーダー
        int offset = savedFilenames.size();
        List<SpotPhoto> existingPhotos = spotPhotoMapper.findBySpotIdAndDisplayOrderGreaterThanEqual(spotId,
                firstDisplayOrder);
        if (!existingPhotos.isEmpty()) {
            for (SpotPhoto sp : existingPhotos) {
                sp.setDisplayOrder(sp.getDisplayOrder() + offset);
            }
            spotPhotoMapper.bulkUpdateDisplayOrder(existingPhotos);
        }

        // 3: 新規レコードのInsert
        int displayOrder = firstDisplayOrder;
        for (String filename : savedFilenames) {
            spotPhotoMapper.insert(SpotPhoto.builder()
                    .spotId(spotId)
                    .displayOrder(displayOrder++)
                    .photoUrl(MyStringUtils.joinPath(imagePrefix, filename))
                    .build());
        }

        return new SaveImagesResult(savedFilenames, errors);
    }

    /**
     * 画像を削除する TODO : ローカルストレージからファイル自体を削除する → バッチ処理でもいい
     */
    @Override
    public void delete(Long id, Long spotId) {
        spotPhotoMapper.delete(id);

        // display_orderを更新する
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
