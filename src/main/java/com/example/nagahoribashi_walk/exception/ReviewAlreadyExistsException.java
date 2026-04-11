package com.example.nagahoribashi_walk.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // 409
public class ReviewAlreadyExistsException extends RuntimeException {

    private final Long spotId;
    private final Long userId;

    public ReviewAlreadyExistsException(String message, Long spotId, Long userId) {
        super(message);
        this.spotId = spotId;
        this.userId = userId;
    }

    public Long getSpotId() {
        return spotId;
    }

    public Long getUserId() {
        return userId;
    }

}