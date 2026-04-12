package com.example.nagahoribashi_walk.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * カテゴリ名またはサブカテゴリ名が重複している場合にスローする例外。
 */
@ResponseStatus(HttpStatus.CONFLICT) // 409
public class CategoryAlreadyExistsException extends RuntimeException {

    public CategoryAlreadyExistsException(String message) {
        super(message);
    }
}
