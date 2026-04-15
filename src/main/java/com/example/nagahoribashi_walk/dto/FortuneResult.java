package com.example.nagahoribashi_walk.dto;

import java.time.LocalDateTime;

import com.example.nagahoribashi_walk.type.FortuneRank;

import lombok.Data;

/**
 * おみくじ結果DTO
 * 
 * @author 海津
 */
@Data
public class FortuneResult {

    // ランク (大吉、中吉、小吉、末吉、凶)
    private FortuneRank rank;

    // おみくじを引いた日時
    private LocalDateTime drawnAt;

    // おすすめスポット
    private SpotSummary recommendedSpot;

    // おすすめスポットがすでにお気に入り登録済みかどうか
    private boolean alreadyFavorited;

    // ランクに応じた一言メッセージ
    private String fortuneMessage;
}