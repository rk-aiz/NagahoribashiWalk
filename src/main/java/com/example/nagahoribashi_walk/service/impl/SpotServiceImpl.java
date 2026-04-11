package com.example.nagahoribashi_walk.service.impl;

import java.util.ArrayList;
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
import com.example.nagahoribashi_walk.repository.SpotMapper;
import com.example.nagahoribashi_walk.service.SpotService;
import com.example.nagahoribashi_walk.util.MyStringUtils;

import lombok.RequiredArgsConstructor;

/**
 * スポット関連サービスの実装クラス
 * 
 * @author 海津, 池田, 篠原
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

    /** ページとキーワードに対応したスポット一覧を返す */
    @Override
    public Page<SpotSummary> searchByKeywords(String keyword, Pageable pageable) {

        // 空文字は一覧にフォールバック
        if (keyword == null || keyword.isBlank()) {
            return getPage(pageable);
        }
        // スペースで分割
        String[] splitKeywords = keyword
                // 全角スペースを半角スペースに置き換え
                .replace('\u3000', ' ')
                .trim().split("\\s+");

        List<List<String>> keywordMatrix = new ArrayList<>();

        for (String kw : splitKeywords) {
            keywordMatrix.add(List.of(kw, MyStringUtils.toHiragana(kw), MyStringUtils.toKatakana(kw)));
        }

        // 件数取得
        long total = spotMapper.countByKeywords(
                keywordMatrix);

        // ヒットしている場合、この条件で取得しreturn
        if (total > 0) {
            // データ取得
            List<SpotSummary> spots = spotMapper.searchByKeywords(
                    keywordMatrix,
                    pageable.getOffset(),
                    pageable.getPageSize());

            return new PageImpl<>(spots, pageable, total);
        }

        // AND検索でヒットしなかった場合、OR検索にフォールバック
        // 全キーワードのバリアントを1つのリストに平坦化することで、すべてORになる
        List<String> allVariants = new ArrayList<>();
        for (String kw : splitKeywords) {
            allVariants.add(kw);
            allVariants.add(MyStringUtils.toHiragana(kw));
            allVariants.add(MyStringUtils.toKatakana(kw));
        }
        List<List<String>> fallbackList = List.of(allVariants);

        long fallbackTotal = spotMapper.countByKeywords(fallbackList);

        if (fallbackTotal == 0) {
            return new PageImpl<>(List.of(), pageable, 0);
        }

        List<SpotSummary> fallbackSpots = spotMapper.searchByKeywords(
                fallbackList,
                pageable.getOffset(),
                pageable.getPageSize());

        return new PageImpl<>(fallbackSpots, pageable, fallbackTotal);

    }

    /** トップページおすすめ３件表示用 */
    @Override
    public List<SpotSummary> getRecommendedSpots() {

        int rand = ThreadLocalRandom.current().nextInt(3);

        return switch (rand) {
            case 0 -> spotMapper.findTopByRating(); // 評価順
            case 1 -> spotMapper.findTopByFavorite(); // お気に入り登録数順
            case 2 -> spotMapper.findRandomSpots(); // ランダム
            default -> spotMapper.findTopByRating(); // 評価順
        };
    }

    // 大谷記載
    @Override
    public Page<SpotSummary> getPageByCategoryId(Long categoryId, Pageable pageable) {

        List<SpotSummary> content = spotMapper.findByCategoryId(
                categoryId, pageable.getOffset(), pageable.getPageSize());

        long total = spotMapper.countByCategoryId(categoryId);

        return new PageImpl<>(content, pageable, total);
    }

    @Override
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
    public SpotDetail getById(Long id, Long loginUserId) {
        spotMapper.incrementPvCount(id);

        SpotDetail spotDetail = spotMapper.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("指定したスポットが存在しません。id=" + id));

        if (loginUserId != null) {
            spotDetail.getReviews().stream().forEach(review -> {
                if (review.getUserId() != null && review.getUserId().equals(loginUserId)) {
                    review.setMyReview(true);
                }
            });
        }

        // 関連スポット追加
        findRelatedSpots(spotDetail);

        if (spotDetail.getAverageRating() != null) {
            double rounded = Math.round(spotDetail.getAverageRating() * 10.0) / 10.0;
            spotDetail.setAverageRating(rounded);
        }

        return spotDetail;
    }

    public void findRelatedSpots(SpotDetail spotDetail) {
        // 関連スポット
        if (spotDetail.getKeywords() != null && !spotDetail.getKeywords().isBlank()) {

            List<String> keywords = Arrays.stream(spotDetail.getKeywords().split(","))
                    .map(String::trim)
                    .filter(k -> !k.isEmpty())
                    .toList();

            List<SpotSummary> relatedSpots = spotMapper.findRandomByAnyKeyword(spotDetail.getId(), keywords, 3);

            spotDetail.setRelatedSpots(relatedSpots);
        }

    }
}
