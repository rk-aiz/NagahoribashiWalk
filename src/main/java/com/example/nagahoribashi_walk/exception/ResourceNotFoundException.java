package com.example.nagahoribashi_walk.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

/**
 * ID(主キー)に対応するリソースがない場合などのException
 */
@Getter
@ResponseStatus(HttpStatus.NOT_FOUND) // 404
public class ResourceNotFoundException extends RuntimeException {

    private final Long id;

    public ResourceNotFoundException(String message, Long id) {
        super(message);
        this.id = id;
    }
}