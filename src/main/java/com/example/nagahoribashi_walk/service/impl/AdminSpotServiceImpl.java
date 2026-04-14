package com.example.nagahoribashi_walk.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagahoribashi_walk.dto.AdminSpotRow;
import com.example.nagahoribashi_walk.entity.Spot;
import com.example.nagahoribashi_walk.repository.AdminSpotMapper;
import com.example.nagahoribashi_walk.service.AdminSpotService;

import lombok.RequiredArgsConstructor;

/**
 * スポット関連サービスの実装クラス
 * 
 * @author 海津, 池田, 篠原
 */
@Service
@Transactional
@RequiredArgsConstructor
public class AdminSpotServiceImpl implements AdminSpotService {

    private final AdminSpotMapper adminSpotMapper;

    /**
     * 【管理者】ページに対応したスポット一覧を返す
     */
    @Override
    public Page<AdminSpotRow> getPageForAdmin(Pageable pageable) {

        // スポットの総数を取得する
        long total = adminSpotMapper.count();

        // 対象ページに対応したスポットを取得する
        List<AdminSpotRow> spots = adminSpotMapper.findAll(pageable.getOffset(), pageable.getPageSize());

        // Page<T>インスタンスに詰めて返す
        return new PageImpl<>(spots, pageable, total);
    }

    /**
     * 【管理者】キーワードでスポットを絞り込んだページを返す
     */
    @Override
    public Page<AdminSpotRow> searchForAdmin(String keyword, String sort, Pageable pageable) {
        long total = adminSpotMapper.countByKeyword(keyword);

        List<AdminSpotRow> spots = adminSpotMapper.findAllByKeyword(
                keyword, sort, pageable.getOffset(), pageable.getPageSize());

        return new PageImpl<>(spots, pageable, total);
    }

    /** 新規スポットを追加する */
    @Override
    public void addSpot(Spot spot) {
        adminSpotMapper.insert(spot);
    }

    /** 【管理者】スポット情報を更新 */
    @Override
    public void updateSpot(Spot spot) {
        adminSpotMapper.update(spot);
    }

    /** 【管理者】スポット情報を論理削除 */
    @Override
    public void softDelete(Long spotId) {
        adminSpotMapper.softDelete(spotId);
    }

    /**
     * 【管理者】スポットをIDから取得する
     */
    @Override
    public Spot getByIdForAdmin(Long spotId) {
        return adminSpotMapper.findEntityById(spotId).orElseThrow();
    }

    /** 【管理者】スポット数を取得 */
    @Override
    public long getSpotCount() {
        return adminSpotMapper.count();
    }

    /** 【管理者】スポットの平均評価を取得 */
    @Override
    public Double getAverageRatingAll() {
        return adminSpotMapper.findAverageRatingAll();
    }

    /** 【管理者】最近登録されたスポットを取得 */
    @Override
    public List<AdminSpotRow> findRecent(int i) {
        return adminSpotMapper.findRecent(i);
    }
}
