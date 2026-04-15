package com.example.nagahoribashi_walk.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.nagahoribashi_walk.util.MyStringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * ファイルストレージサービス
 * アップロードファイルの保存・削除を担当する
 *
 * @author 海津
 */
@Slf4j
@Service
public class FileStorageService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    private static final String IMAGE_PREFIX = "images";

    /**
     * 画像ファイルを保存し、相対パス（例: images/uuid.jpg）を返す。
     * 空ファイルまたは画像以外の場合は null を返す。
     */
    public String saveImage(MultipartFile file) throws IOException {

        if (file.isEmpty() || !Optional.ofNullable(file.getContentType()).orElse("").startsWith("image/")) {
            return null;
        }

        Path uploadPath = Paths.get(MyStringUtils.joinPath(uploadDir, IMAGE_PREFIX));
        Files.createDirectories(uploadPath);

        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String newFilename = UUID.randomUUID() + "." + extension;

        Files.copy(file.getInputStream(), uploadPath.resolve(newFilename), StandardCopyOption.REPLACE_EXISTING);

        return MyStringUtils.joinPath(IMAGE_PREFIX, newFilename);
    }

    /**
     * 相対パス（例: images/uuid.jpg）のファイル実体を削除する。
     * 削除対象が存在しない場合は何もしない。
     *
     * @param photoUrl spot_photos.photo_url に保存されている相対パス
     */
    public void deleteImage(String photoUrl) {
        Path p = Paths.get(MyStringUtils.joinPath(uploadDir, photoUrl));
        try {
            Files.deleteIfExists(p);
        } catch (IOException e) {
            log.error("ファイルの削除に失敗しました: {}", e.getLocalizedMessage());
        }
    }
}
