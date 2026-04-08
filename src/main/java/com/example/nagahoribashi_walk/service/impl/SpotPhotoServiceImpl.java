package com.example.nagahoribashi_walk.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

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

	/** 【管理者】画像ファイルを保存する */
	@Override
    public String saveImage(MultipartFile file) throws IOException {
        // 保存先ディレクトリを作成（存在しない場合）
        Path uploadPath = Paths.get(uploadDir + "/images");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // ファイル名の重複を避けるためUUIDを使用
        String originalFilename = file.getOriginalFilename();
        String extension = StringUtils.getFilenameExtension(originalFilename);
        String newFilename = UUID.randomUUID() + "." + extension;

        // ファイルを保存
        Path filePath = uploadPath.resolve(newFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return newFilename;
    }

	@Override
	public void delete(Long id) {
		spotPhotoMapper.delete(id);
	}
}
