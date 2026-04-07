package com.example.nagahoribashi_walk.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagahoribashi_walk.dto.AdminSpotRow;
import com.example.nagahoribashi_walk.dto.SpotDetail;
import com.example.nagahoribashi_walk.dto.SpotSummary;
import com.example.nagahoribashi_walk.entity.Spot;
import com.example.nagahoribashi_walk.repository.SpotMapper;
import com.example.nagahoribashi_walk.service.SpotService;

import lombok.RequiredArgsConstructor;

/**
 * スポット関連サービスの実装クラス
 * 
 * @author 海津
 */
@Service
@Transactional
@RequiredArgsConstructor
public class SpotServiceImpl implements SpotService {

    private final SpotMapper spotMapper;

    /**
     * ページに対応したスポット一覧を返す
     */
    @Override
    public Page<SpotSummary> getPage(Pageable pageable) {

        // スポットの総数を取得する
        long total = spotMapper.count();

        // 対象ページに対応したスポットを取得する
        List<SpotSummary> spots = spotMapper.findAll(pageable.getOffset(), pageable.getPageSize());

        // Page<T>インスタンスに詰めて返す
        return new PageImpl<>(spots, pageable, total);
    }

    /**
     * カタカナ → ひらがな変換
     */
    private String toHiragana(String input) {
        if (input == null)
            return null;

        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            // カタカナ範囲ならひらがなへ変換
            if (c >= 'ァ' && c <= 'ン') {
                sb.append((char) (c - 0x60));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    
    /**
     * ひらがな → カタカナ変換
     */
    private String toKatakana(String input) {
        if (input == null)
            return null;
        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            // ひらがな範囲ならカタカナへ変換
            if (c >= 'ぁ' && c <= 'ん') {
                sb.append((char) (c + 0x60));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    
    /**
     * ページとキーワードに対応したスポット一覧を返す
     */
    @Override
    public Page<SpotSummary> searchByKeywords(String keyword, Pageable pageable) {
	
	    // 🔸 スペースで分割
	    String[] splitKeywords = keyword.trim().replace('　', ' ').split("\\s+");
	
	    List<Map<String, String>> keywordMapList = new ArrayList<>();
	    
	    for (String kw : splitKeywords) {
	    	
	    	Map<String, String> map = new HashMap<>();
	    	map.put("origin", kw);
	    	map.put("hira", toHiragana(kw));
	    	map.put("kana", toKatakana(kw));
	    	keywordMapList.add(map);
	    }
	    
	    

	    
        // 空文字は一覧にフォールバック
        if (keyword == null || keyword.isBlank()) {
            return getPage(pageable);
        }
	    // 🔸 件数取得
	    long total = spotMapper.countByKeywords(
	            keywordMapList
	    );

	    // 🔸 データ取得
	    List<SpotSummary> spots =
	            spotMapper.searchByKeywords(
	            		keywordMapList,
	                    pageable.getOffset(),
	                    pageable.getPageSize()
	            );

	    return new PageImpl<>(spots, pageable, total);
	}



    // トップページおすすめ３件表示用
    @Override
    public List<SpotSummary> getRecommendedSpots() {
        return spotMapper.findRecommendedSpots();
    }

    // 大谷記載
    @Override
    public Page<SpotSummary> getPageByCategoryId(Long categoryId, Pageable pageable) {

        List<SpotSummary> content = spotMapper.findByCategoryId(
                categoryId, pageable.getOffset(), pageable.getPageSize());

        long total = spotMapper.countByCategoryId(categoryId);

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<SpotSummary> getPageBySubCategoryId(Long subCategoryId, Pageable pageable) {

        List<SpotSummary> content = spotMapper.findBySubCategoryId(
                subCategoryId, pageable.getOffset(), pageable.getPageSize());

        long total = spotMapper.countBySubCategoryId(subCategoryId);

        return new PageImpl<>(content, pageable, total);
    }

    /**
     * IDに対応したスポットの詳細を取得
     */
    @Override
    public SpotDetail findById(Long id, Long loginUserId) {
        SpotDetail spotDetail = spotMapper.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("指定したスポットが存在しません。id=" + id));

        if (loginUserId != null) {
            spotDetail.getReviews().stream().forEach(review -> {
                if (review.getUserId() != null && review.getUserId().equals(loginUserId)){
                    review.setMyReview(true);
                }
            });
        }

        if (spotDetail.getAverageRating() != null) {
            double rounded = Math.round(spotDetail.getAverageRating() * 10.0) / 10.0;
            spotDetail.setAverageRating(rounded);
        }

        return spotDetail;
    }
    
    /**
     * 【管理者】ページに対応したスポット一覧を返す
     */
    @Override
    public Page<AdminSpotRow> getPageForAdmin(Pageable pageable) {

        // スポットの総数を取得する
        long total = spotMapper.count();

        // 対象ページに対応したスポットを取得する
        List<AdminSpotRow> spots = spotMapper.findAllForAdmin(pageable.getOffset(), pageable.getPageSize());

        // Page<T>インスタンスに詰めて返す
        return new PageImpl<>(spots, pageable, total);
    }

    @Override
    public void addSpot(Spot spot) {
        spotMapper.insert(spot);
    }

    @Override
    public void updateSpot(Spot spot) {
        spotMapper.update(spot);
    }
}
