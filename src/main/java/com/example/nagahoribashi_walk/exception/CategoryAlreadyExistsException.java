package com.example.nagahoribashi_walk.exception;

/**
 * カテゴリ名またはサブカテゴリ名が重複している場合にスローする例外。
 */
public class CategoryAlreadyExistsException extends RuntimeException {

    public CategoryAlreadyExistsException(String message) {
        super(message);
    }
}
