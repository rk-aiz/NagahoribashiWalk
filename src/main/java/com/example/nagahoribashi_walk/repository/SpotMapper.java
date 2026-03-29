package com.example.nagahoribashi_walk.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.nagahoribashi_walk.dto.SpotSummary;

/**
 * spotsテーブルに対応したMapperのインターフェース
 * 
 * @author 海津
 */
@Mapper
public interface SpotMapper {

    List<SpotSummary> findAll(@Param("offset") long offset, @Param("limit") int limit);

    // findById

    // findByCategoryId

    // findBySubCategoryId

    // searchByKeywords findImagesBySpotId

    // findAllForAdmin

    // insert

    // update

    // softDelete

    // insertImage

    // deleteImage

}
