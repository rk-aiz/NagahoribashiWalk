package com.example.nagahoribashi_walk.service;

import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.nagahoribashi_walk.dto.AdminSpotRow;
import com.example.nagahoribashi_walk.dto.SpotDetail;
import com.example.nagahoribashi_walk.dto.SpotSummary;
import com.example.nagahoribashi_walk.entity.Spot;

/**
 * スポット関連サービスのインターフェース
 * 
 * @author 海津
 */
public interface SpotService {

    /**
     * 【一般 & ユーザー】スポット詳細情報を取得
     * ・スポットに対して投稿されたレビュー一覧も取得
     * ・ユーザーの場合は自身のレビューにフラグを付ける
     */
    SpotDetail findById(Long id, Long loginUserId);

    /** 【一般】スポット一覧をページネーションで取得 */
    Page<SpotSummary> getPage(Pageable pageable);

    /** 【一般】キーワードにヒットしたスポットをページネーションで取得 */
    Page<SpotSummary> searchByKeywords(String keyword, Pageable pageable);

    /** 【一般】おすすめスポットを取得 */
    List<SpotSummary> getRecommendedSpots();

    /** 【一般】対象カテゴリに属するスポットをページネーションで取得 */
    Page<SpotSummary> getPageByCategoryId(Long categoryId, Pageable pageable);

    /** 【一般】対象サブカテゴリに属するスポットをページネーションで取得 */
    Page<SpotSummary> getPageBySubCategoryId(Long SubcategoryId, Pageable pageable);

    // =============== 管理者用 ===============

    /** 【管理者】スポット情報をIDから取得する */
    Spot getByIdForAdmin(Long spotId);

    /** 【管理者】管理者用にスポット一覧をページネーションで取得 */
    Page<AdminSpotRow> getPageForAdmin(Pageable pageable);

    /** 【管理者】管理者用にキーワードにヒットしたスポットをページネーションで取得 */
    Page<AdminSpotRow> searchForAdmin(String keyword, Pageable pageable);

    /** 【管理者】新規スポットを登録 */
    void addSpot(Spot spot);

    /** 【管理者】スポット情報を更新 */
    void updateSpot(Spot spot);

    /** 【管理者】スポット情報を論理削除 */
    void softDelete(Long id);

    long getSpotCount();

    Double getAverageRatingAll();

    List<AdminSpotRow> findRecent(int i);
}
