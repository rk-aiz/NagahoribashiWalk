package com.example.nagahoribashi_walk.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagahoribashi_walk.dto.FortuneResult;
import com.example.nagahoribashi_walk.dto.SpotSummary;
import com.example.nagahoribashi_walk.entity.FortuneTheme;
import com.example.nagahoribashi_walk.entity.User;
import com.example.nagahoribashi_walk.exception.InvalidRequestException;
import com.example.nagahoribashi_walk.exception.ResourceNotFoundException;
import com.example.nagahoribashi_walk.repository.FavoriteMapper;
import com.example.nagahoribashi_walk.repository.FortuneThemeRepository;
import com.example.nagahoribashi_walk.repository.SpotMapper;
import com.example.nagahoribashi_walk.repository.UserMapper;
import com.example.nagahoribashi_walk.service.FortuneSlipService;
import com.example.nagahoribashi_walk.service.userdetails.LoginUser;
import com.example.nagahoribashi_walk.type.FortuneRank;

import lombok.RequiredArgsConstructor;

/**
 * おみくじ関連サービスの実装クラス
 * 
 * @author 海津
 */
@Service
@Transactional
@RequiredArgsConstructor
public class FortuneSlipServiceImpl implements FortuneSlipService {

    /** おみくじ後におすすめされたスポットをお気に入り登録した際のポイント。application.propertiesから取得 */
    @Value("${fortune.bonus-point}")
    private int fortuneBonusPoint;

    /** サーバー起動ごとに変わるシード値 */
    @Qualifier("serverStartupSeed")
    private final Long serverSeed;

    /* リポジトリを注入 */
    private final UserMapper userMapper;
    private final FavoriteMapper favoriteMapper;
    private final FortuneThemeRepository fortuneThemeRepository; // <- Java実装なのでTransaction管理なし
    private final SpotMapper spotMapper;

    /** ユーザーが既に当日分のおみくじを引き済みか判定する */
    @Override
    @Transactional(readOnly = true)
    public boolean isAlreadyDrawn(LoginUser user) {
        return userMapper.findById(user.getId())
                .map(User::getLastDrawnAt)
                .map(t -> t.toLocalDate().isEqual(LocalDate.now()))
                .orElse(false);
    }

    /** 気分の選択肢を取得する（当日固定・4件） */
    @Override
    public Map<Long, String> getMoodSelection(LoginUser user) {
        // 日付ベースのシードでその日の選択肢を固定する
        // 同じユーザーが同じ日に何度訪れても同じ4件が表示される
        return fortuneThemeRepository
                .findRandom(4, new Random(buildDailySeed(user.getUsername())))
                .stream()
                .collect(Collectors.toMap(FortuneTheme::getId, FortuneTheme::getMood));
    }

    /**
     * おみくじを引く（ランク決定・推薦スポット決定・ポイント付与・DB更新）
     */
    @Override
    public void draw(Long themeId, LoginUser loginUser) {

        // 引いた日時をJava側で確定させる（ランク再現シードとDB保存値を一致させるため）
        LocalDateTime drawnAt = LocalDateTime.now();

        // 提示された選択肢を選んでいない場合は例外 (不正なPOSTリクエストを弾くため)
        if (!this.getMoodSelection(loginUser).containsKey(themeId)) {
            throw new InvalidRequestException("不正な値が送信されました");
        }

        // 選択された気分のキーワードを取得（無効なthemeIdの場合は空文字列で続行）
        String keywords = fortuneThemeRepository.findById(themeId).map(t -> t.getKeywords()).orElse("");

        // キーワードのいずれかに一致するスポットをランダムに1件取得
        List<SpotSummary> recommends = spotMapper.findRandomByAnyKeywords(0L,
                Arrays.stream(keywords.split(","))
                        .map(String::trim)
                        .filter(k -> !k.isEmpty())
                        .toList(),
                1);

        // キーワードに一致するスポットがなければ全スポットからランダムにフォールバック
        if (recommends.isEmpty()) {
            recommends.addAll(spotMapper.findRandomSpots(1));
        }

        if (recommends.isEmpty()) {
            throw new ResourceNotFoundException("おすすめスポットを見つけられませんでした", themeId);
        }

        // おみくじランクを決定する（drawnAtがシードなので決定的・再現する）
        FortuneRank rank = FortuneRank.draw(new Random(buildDrawSeed(loginUser.getUsername(), drawnAt))::nextInt);

        Long recommendedSpotId = recommends.getFirst().getId();

        // 推薦スポットIDと引いた日時をDBに保存
        userMapper.updateFortuneSlip(loginUser.getId(), recommendedSpotId, drawnAt);

        // ランクに応じたポイントを付与
        userMapper.addPoint(loginUser.getId(), rank.getPoint());

        // 推薦スポットをすでにお気に入り登録済みであれば、引いた時点でボーナスを付与する
        if (favoriteMapper.existsByUserAndSpot(loginUser.getId(), recommendedSpotId)) {
            userMapper.addPoint(loginUser.getId(), fortuneBonusPoint);
        }
    }

    /**
     * おみくじ結果を取得する（ランクはシードから再現）
     */
    @Override
    @Transactional(readOnly = true)
    public FortuneResult getFortuneResult(LoginUser loginUser) {

        // LoginUserはセッションキャッシュのため、DBから最新のユーザー情報を取得する
        User freshUser = userMapper.findById(loginUser.getId()).orElseThrow();
        LocalDateTime lastDrawnAt = freshUser.getLastDrawnAt();

        // recommended_spot_id からスポット情報を取得
        // スポットが論理削除されている場合はnull
        SpotSummary spotSummary = spotMapper.findSummaryById(freshUser.getRecommendedSpotId()).orElse(null);

        // お気に入り登録済みかどうかを確認（スポットが存在する場合のみ）
        boolean alreadyFavorited = spotSummary != null
                && favoriteMapper.existsByUserAndSpot(loginUser.getId(), spotSummary.getId());

        // ランク(大吉や中吉など)は「ユーザー名 + 引いた日時」のシードから再現する
        // draw()でも同じシードを使っているため、何度ページを開いても同じランクが表示される
        long drawSeed = buildDrawSeed(loginUser.getUsername(), lastDrawnAt);
        FortuneRank rank = FortuneRank.draw(new Random(drawSeed)::nextInt);

        // おみくじ結果用のDTOに詰めて返す
        FortuneResult result = new FortuneResult();
        result.setRecommendedSpot(spotSummary);
        result.setDrawnAt(lastDrawnAt);
        result.setAlreadyFavorited(alreadyFavorited);
        result.setRank(rank);
        result.setFortuneMessage(rank.pickMessage(drawSeed + 1));

        return result;
    }

    /** 気分選択肢用のシード値を用意する (日付ベースで固定にする) */
    private long buildDailySeed(String preString) {
        return (31L * preString.hashCode()) + serverSeed + LocalDate.now().toEpochDay();
    }

    /**
     * おみくじ結果ランク用のシード値を用意する (プレフィックス + 引いた日時)
     */
    private long buildDrawSeed(String preString, LocalDateTime drawTime) {
        return preString.hashCode() + drawTime.toEpochSecond(ZoneOffset.UTC);
    }

}
