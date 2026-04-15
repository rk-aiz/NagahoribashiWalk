package com.example.nagahoribashi_walk.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

/**
 * 同一スポットに対して同一ユーザーが既にレビューを投稿済みの場合にスローされる例外（HTTP 409）
 */
@Getter
@ResponseStatus(HttpStatus.CONFLICT) // 409
public class ReviewAlreadyExistsException extends RuntimeException {

    private final Long spotId;
    private final Long userId;

    public ReviewAlreadyExistsException(String message, Long spotId, Long userId) {
        super(message);
        this.spotId = spotId;
        this.userId = userId;
    }
}