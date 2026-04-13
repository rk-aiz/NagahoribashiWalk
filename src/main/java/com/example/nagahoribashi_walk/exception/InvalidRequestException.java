package com.example.nagahoribashi_walk.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 想定外のPOSTリクエストなど、不正なリクエスト値が送信された場合のException
 */
@ResponseStatus(HttpStatus.BAD_REQUEST) // 400
public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException(String message) {
        super(message);
    }
}
