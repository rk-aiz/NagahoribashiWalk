package com.example.nagahoribashi_walk.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * ユーザー名またはメールアドレスが既に登録済みの場合にスローされる例外（HTTP 409）
 */
@ResponseStatus(HttpStatus.CONFLICT) // 409
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}