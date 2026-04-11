package com.example.nagahoribashi_walk.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.nagahoribashi_walk.dto.SpotDetail;
import com.example.nagahoribashi_walk.dto.SpotSummary;

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
    SpotDetail getById(Long id, Long loginUserId);

    /** 【一般】スポット一覧をページネーションで取得 */
    Page<SpotSummary> getPage(Pageable pageable);

    /** 【一般】キーワードにヒットしたスポットをページネーションで取得 */
    Page<SpotSummary> searchByKeywords(String keyword, Pageable pageable);

    /** 【一般】おすすめスポットを取得 */
    List<SpotSummary> getRecommendedSpots();

    /** 【一般】対象カテゴリに属するスポットをページネーションで取得 */
    Page<SpotSummary> getPageByCategoryId(Long categoryId, Pageable pageable);

    /** 【一般】対象サブカテゴリに属するスポットをページネーションで取得 */
    Page<SpotSummary> getPageBySubCategoryId(Long subcategoryId, Pageable pageable);

    /** スポットIDとキーワードをもとに、関連するスポットを取得する */
    List<SpotSummary> findRelatedSpots(Long spotId, String keyword);

}
