package com.example.nagahoribashi_walk.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.nagahoribashi_walk.dto.SpotSummary;

/**
 * スポット関連サービスのインターフェース
 * 
 * @author 海津
 */
public interface SpotService {

    Page<SpotSummary> getPage(Pageable pageable);
    
    Page<SpotSummary> searchByKeywords(String keyword, Pageable pageable);

    List<SpotSummary> getRecommendedSpots();
    
    Page<SpotSummary> getPageByCategoryId(Long categoryId, Pageable pageable);
    
    Page<SpotSummary> getPageBySubCategoryId(Long SubcategoryId, Pageable pageable);
    
}

