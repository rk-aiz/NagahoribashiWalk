package com.example.nagahoribashi_walk.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.nagahoribashi_walk.type.FortuneRank;

import lombok.Data;

/**
 * おみくじ結果DTO
 */
@Data
public class FortuneResult {

    private FortuneRank rank;

    private LocalDateTime drawnAt;

    private SpotSummary recommendedSpot;

    /** おすすめスポットがすでにお気に入り登録済みかどうか */
    private boolean alreadyFavorited;

    /** ランクに応じた一言メッセージ */
    private String fortuneMessage;

    public boolean getIsAlreadyDrawnToday() {
        return drawnAt.toLocalDate().isEqual(LocalDate.now());
    }
}