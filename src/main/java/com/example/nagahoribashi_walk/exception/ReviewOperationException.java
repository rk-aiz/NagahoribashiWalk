package com.example.nagahoribashi_walk.exception;

import lombok.Getter;

/**
 * レビュー操作（更新・削除）時のビジネスロジックエラー。
 * spotId を持ち、エラー後のリダイレクト先の決定に使用する。
 */
@Getter
public class ReviewOperationException extends RuntimeException {

    private final Long spotId;

    public ReviewOperationException(String message, Long spotId) {
        super(message);
        this.spotId = spotId;
    }

    public Long getSpotId() {
        return spotId;
    }
}
