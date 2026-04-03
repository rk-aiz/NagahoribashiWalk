package com.example.nagahoribashi_walk.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.nagahoribashi_walk.dto.SpotDetail;
import com.example.nagahoribashi_walk.dto.SpotSummary;
import com.example.nagahoribashi_walk.entity.Spot;

/**
 * スポット関連サービスのインターフェース
 * 
 * @author 海津
 */
public interface SpotService {

    Page<SpotSummary> getPage(Pageable pageable);

    Page<SpotSummary> searchByKeywords(String keyword, Pageable pageable);

    List<SpotSummary> getRecommendedSpots();

    void addSpot(Spot spot);

    void updateSpot(Spot spot);

    Page<SpotSummary> getPageByCategoryId(Long categoryId, Pageable pageable);

    Page<SpotSummary> getPageBySubCategoryId(Long SubcategoryId, Pageable pageable);

    SpotDetail findById(Long id, Long loginUserId);

}
