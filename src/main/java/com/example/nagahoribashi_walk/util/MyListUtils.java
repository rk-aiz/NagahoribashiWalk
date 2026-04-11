package com.example.nagahoribashi_walk.util;

import java.util.List;

/**
 * 文字列関連のユーティリティクラス
 *
 * @author 海津
 */
public class MyListUtils {

    private MyListUtils() {
    }

    public static <T> List<T> flatMatrix(List<List<T>> matrix) {
        return matrix.stream()
                .flatMap(List::stream)
                .toList();
    }

}