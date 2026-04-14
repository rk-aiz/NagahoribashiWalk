package com.example.nagahoribashi_walk.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagahoribashi_walk.dto.SpotDetail;
import com.example.nagahoribashi_walk.dto.SpotSummary;
import com.example.nagahoribashi_walk.exception.ResourceNotFoundException;
import com.example.nagahoribashi_walk.repository.SpotMapper;
import com.example.nagahoribashi_walk.service.SpotService;
import com.example.nagahoribashi_walk.util.MyListUtils;
import com.example.nagahoribashi_walk.util.MyStringUtils;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;

/**
 * スポット関連サービスの実装クラス
 * 
 * @author 海津, 池田, 篠原, 大谷
 */
@Service
@Transactional
@RequiredArgsConstructor
public class SpotServiceImpl implements SpotService {

    private final SpotMapper spotMapper;

    /** スポット一覧(ページ)を返す */
    @Override
    @Transactional(readOnly = true)
    public Page<SpotSummary> getPage(Pageable pageable) {

        // スポットの総数を取得する
        long total = spotMapper.count();

        // 対象ページに対応したスポットを取得する
        List<SpotSummary> spots = spotMapper.findAll(pageable.getOffset(), pageable.getPageSize());

        // Page<T>インスタンスに詰めて返す
        return new PageImpl<>(spots, pageable, total);
    }

    /** トップページおすすめ３件表示用 */
    @Override
    @Transactional(readOnly = true)
    public List<SpotSummary> getRecommendedSpots() {

        int rand = ThreadLocalRandom.current().nextInt(3);

        return switch (rand) {
            case 0 -> spotMapper.findTopByRating(3); // 評価順
            case 1 -> spotMapper.findTopByFavorite(3); // お気に入り登録数順
            case 2 -> spotMapper.findRandomSpots(3); // ランダム
            default -> spotMapper.findTopByRating(3); // 評価順
        };
    }

    /** カテゴリIDに対応したスポット一覧を取得(ページ) */
    @Override
    @Transactional(readOnly = true)
    public Page<SpotSummary> getPageByCategoryId(Long categoryId, Pageable pageable) {

        List<SpotSummary> content = spotMapper.findByCategoryId(
                categoryId, pageable.getOffset(), pageable.getPageSize());

        long total = spotMapper.countByCategoryId(categoryId);

        return new PageImpl<>(content, pageable, total);
    }

    /** サブテゴリIDに対応したスポット一覧を取得(ページ) */
    @Override
    @Transactional(readOnly = true)
    public Page<SpotSummary> getPageBySubCategoryId(Long subCategoryId, Pageable pageable) {

        List<SpotSummary> content = spotMapper.findBySubCategoryId(
                subCategoryId, pageable.getOffset(), pageable.getPageSize());

        long total = spotMapper.countBySubCategoryId(subCategoryId);

        return new PageImpl<>(content, pageable, total);
    }

    /**
     * スポットIDから詳細(DTO)を取得
     * 
     * @param loginUserId は、該当スポットについたレビューの中に、自身のレビューがあるか判定する用
     */
    @Override
    public SpotDetail getById(Long id, @Nullable Long loginUserId) {

        SpotDetail spotDetail = spotMapper.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("指定したスポットが存在しません", id));

        spotMapper.incrementPvCount(id);

        if (loginUserId != null) {
            spotDetail.getReviews().stream().forEach(review -> {
                if (review.getUserId() != null && review.getUserId().equals(loginUserId)) {
                    review.setMyReview(true);
                }
            });
        }

        if (spotDetail.getAverageRating() != null) {
            double rounded = Math.round(spotDetail.getAverageRating() * 10.0) / 10.0;
            spotDetail.setAverageRating(rounded);
        }

        return spotDetail;
    }

    /** スポットIDとキーワードをもとに、関連するスポットを取得する */
    @Override
    @Transactional(readOnly = true)
    public List<SpotSummary> findRelatedSpots(Long spotId, String keywords) {

        if (keywords == null || keywords.isBlank()) {
            return List.of();
        }

        return spotMapper.findRandomByAnyKeywords(
                spotId,
                Arrays.stream(keywords.split(","))
                        .map(String::trim)
                        .filter(k -> !k.isEmpty())
                        .toList(),
                3);
    }

    /** ページとキーワードに対応したスポット一覧を返す */
    @Override
    @Transactional(readOnly = true)
    public Page<SpotSummary> searchByKeywords(String keywords, Pageable pageable) {

        // 1. 空文字検索は全取得にフォールバック
        if (keywords == null || keywords.isBlank()) {
            return getPage(pageable);
        }

        // 2. 検索ワードリストを、カタカナ・ひらがなのバリアントを用意したマトリックスに変換
        List<List<String>> keywordMatrix = buildKeywordMatrix(keywords);

        // 3. AND検索でのヒット件数取得
        long total = spotMapper.countByKeywords(keywordMatrix);

        // 4. AND検索にヒットがあれば、AND検索結果をreturn
        if (total > 0) {
            return searchByKeywordsCore(keywordMatrix, pageable, total);
        }

        // 5. AND検索にヒットがなければ、OR検索にフォールバック
        List<List<String>> fallback = List.of(MyListUtils.flatMatrix(keywordMatrix));
        long fallbackTotal = spotMapper.countByKeywords(fallback);
        return searchByKeywordsCore(fallback, pageable, fallbackTotal);
    }

    /**
     * keywordMatrix と件数をもとにスポット一覧(ページ)を返す。
     * 件数が0の場合は空ページを返す。
     */
    private Page<SpotSummary> searchByKeywordsCore(List<List<String>> keywordMatrix, Pageable pageable, Long total) {
        List<SpotSummary> spots = switch (total.compareTo(0L)) {
            case 0 -> List.of();
            default -> spotMapper.searchByKeywords(
                    keywordMatrix,
                    pageable.getOffset(),
                    pageable.getPageSize());
        };
        return new PageImpl<>(spots, pageable, total);
    }

    /**
     * キーワード文字列をスペースで分割し、各キーワードを
     * [原文, ひらがな, カタカナ] のバリアントリストに変換して返す。
     *
     * 例: "カフェ" → [["なんばグランド", "なんばぐらんど", "ナンバグランド"], ...]
     */
    private List<List<String>> buildKeywordMatrix(String keywords) {

        return Arrays.stream(keywords
                .replace('\u3000', ' ') // 全角スペースを半角スペースに置き換え
                .trim().split("\\s+")) // スペースで分割
                .filter(kw -> kw != null && !kw.isBlank()) // 空ワードをフィルター
                .map(kw -> List.of(kw, MyStringUtils.toHiragana(kw), MyStringUtils.toKatakana(kw))) // バリアント作成
                .toList();
    }
}
