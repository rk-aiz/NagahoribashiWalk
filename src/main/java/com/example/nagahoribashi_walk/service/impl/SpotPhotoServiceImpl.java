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

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SpotPhotoServiceImpl implements SpotPhotoService {

	@Value("${app.upload.dir}")
    private String uploadDir;

	private final SpotPhotoMapper spotPhotoMapper;
	
	/** 【管理者】スポットIDに対応する画像一覧を取得する */
	@Override
	public List<SpotPhoto> getAllBySpotId(Long spotId) {
		return spotPhotoMapper.findAllBySpotId(spotId);
	}

	/** 【管理者】画像ファイル一覧を保存する */
	@Override
    public SaveImagesResult saveImages(List<MultipartFile> files, Long spotId, Integer firstDisplayOrder) {
        List<String> savedFilenames = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        // 保存先ディレクトリを作成（存在しない場合）
        Path uploadPath = Paths.get(uploadDir, "images");
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
        List<SpotPhoto> existingPhotos = spotPhotoMapper.findBySpotIdAndDisplayOrderGreaterThanEqual(spotId, firstDisplayOrder);
        if (!existingPhotos.isEmpty()) {
            for (SpotPhoto sp : existingPhotos) {
                sp.setDisplayOrder(sp.getDisplayOrder() + 1);
            }
            spotPhotoMapper.bulkUpdateDisplayOrder(existingPhotos);
        }

        // 3: 新規レコードのInsert
        int displayOrder = firstDisplayOrder;
        for (String filename : savedFilenames) {
                spotPhotoMapper.insert(SpotPhoto.builder()
                        .spotId(spotId)
                        .displayOrder(displayOrder++)
                        .photoUrl(Paths.get("images", filename).toString())
                        .build());
        }

        return new SaveImagesResult(savedFilenames, errors);
    }

	@Override
	public void delete(Long id) {
		spotPhotoMapper.delete(id);
	}
}
