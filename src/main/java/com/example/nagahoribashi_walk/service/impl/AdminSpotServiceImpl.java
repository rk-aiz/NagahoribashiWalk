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
        // TODO : gmapUrlの検証処理(Google Map埋め込みのURLが正しい形か)を追加する -> (iframeで使用するため)
        adminSpotMapper.insert(spot);
    }

    @Override
    public void updateSpot(Spot spot) {
        adminSpotMapper.update(spot);
    }

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

    @Override
    public long getSpotCount() {
        return adminSpotMapper.count();
    }

    @Override
    public Double getAverageRatingAll() {
        return adminSpotMapper.findAverageRatingAll();
    }

    @Override
    public List<AdminSpotRow> findRecent(int i) {
        return adminSpotMapper.findRecent(i);
    }
}
