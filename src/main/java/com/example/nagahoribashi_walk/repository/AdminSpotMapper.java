package com.example.nagahoribashi_walk.repository;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.nagahoribashi_walk.dto.AdminSpotRow;
import com.example.nagahoribashi_walk.entity.Spot;

/**
 * spotsテーブルに対応したMapperのインターフェース(管理者機能)
 * 
 * @author 海津
 */
@Mapper
public interface AdminSpotMapper {

    List<AdminSpotRow> findAllForAdmin(
            @Param("offset") long offset, @Param("limit") int limit);

    List<AdminSpotRow> findAllForAdminByKeyword(
            @Param("keyword") String keyword,
            @Param("sort") String sort,
            @Param("offset") long offset,
            @Param("limit") int limit);

    long countForAdminByKeyword(@Param("keyword") String keyword);

    void insert(Spot spot);

    void update(Spot spot);

    Optional<Spot> findByIdForAdmin(@Param("spotId") Long spotId);

    void softDelete(@Param("spotId") Long spotId);

    /** スポット数をカウントする */
    long count();

    /** 全スポットの平均評価を取得する（小数第1位） */
    Double findAverageRatingAll();

    /** 最近登録されたスポットをN件取得する */
    List<AdminSpotRow> findRecent(@Param("limit") int limit);
}
