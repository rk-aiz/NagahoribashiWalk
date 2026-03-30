package com.example.nagahoribashi_walk.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagahoribashi_walk.dto.SpotSummary;
import com.example.nagahoribashi_walk.repository.SpotMapper;
import com.example.nagahoribashi_walk.service.SpotService;

import lombok.RequiredArgsConstructor;

/**
 * スポット関連サービスの実装クラス
 * 
 * @author 海津
 */
@Service
@Transactional
@RequiredArgsConstructor
public class SpotServiceImpl implements SpotService {

    private final SpotMapper spotMapper;

    /**
     * ページに対応したスポット一覧を返す
     */
    @Override
    public Page<SpotSummary> getPage(Pageable pageable) {

        // スポットの総数を取得する
        long total = spotMapper.count();

        // 対象ページに対応したスポットを取得する
        List<SpotSummary> spots = spotMapper.findAll(pageable.getOffset(), pageable.getPageSize());

        // Page<T>インスタンスに詰めて返す
        return new PageImpl<>(spots, pageable, total);
    }
}
