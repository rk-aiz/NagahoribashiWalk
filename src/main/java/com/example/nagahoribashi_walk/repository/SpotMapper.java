package com.example.nagahoribashi_walk.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.nagahoribashi_walk.dto.SpotSummary;
import com.example.nagahoribashi_walk.entity.Spot;

/**
 * spotsテーブルに対応したMapperのインターフェース
 * 
 * @author 海津
 */
@Mapper
public interface SpotMapper {

    /** ページネーション付きでスポット一覧を取得する */
    List<SpotSummary> findAll(@Param("offset") long offset, @Param("limit") int limit);

    List<SpotSummary> findRecommendedSpots();
    
    // findById

    // findByCategoryId

    // findBySubCategoryId

    List<SpotSummary> searchByKeywords(
    		@Param("keyword") String keyword,
    		@Param("offset") long offset,
    		@Param("limit") int limit);

    // findImagesBySpotId

    // findAllForAdmin

     void insert(Spot spot);
    
    // update

    // softDelete

    // insertImage

    // deleteImage

    /** スポット数をカウントする */
    long count();
    
    long countByKeywords(@Param("keyword") String keyword);
}
