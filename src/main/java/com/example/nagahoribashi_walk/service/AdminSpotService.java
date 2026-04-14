package com.example.nagahoribashi_walk.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.nagahoribashi_walk.dto.AdminSpotRow;
import com.example.nagahoribashi_walk.entity.Spot;

/**
 * スポット関連サービスのインターフェース
 * 
 * @author 海津, 池田, 篠原, 大谷
 */
public interface AdminSpotService {

    /** 【管理者】スポット情報をIDから取得する */
    Spot getByIdForAdmin(Long spotId);

    /** 【管理者】管理者用にスポット一覧をページネーションで取得 */
    Page<AdminSpotRow> getPageForAdmin(Pageable pageable);

    /** 【管理者】管理者用にキーワードにヒットしたスポットをページネーションで取得 */
    Page<AdminSpotRow> searchForAdmin(String keyword, String sort, Pageable pageable);

    /** 【管理者】新規スポットを登録 */
    void addSpot(Spot spot);

    /** 【管理者】スポット情報を更新 */
    void updateSpot(Spot spot);

    /** 【管理者】スポット情報を論理削除 */
    void softDelete(Long id);

    /** 【管理者】スポット数を取得 */
    long getSpotCount();

    /** 【管理者】スポットの平均評価を取得 */
    Double getAverageRatingAll();

    /** 【管理者】最近登録されたスポットを取得 */
    List<AdminSpotRow> findRecent(int i);
}
