package com.example.nagahoribashi_walk.util;

/**
 * 文字列関連のユーティリティクラス
 *
 * @author 海津
 */
public class StringUtils {

    private StringUtils() {}

    /**
     * 文字列の末尾に"/"かあるか確認し、なければ付与する
     */
    public static String ensureTrailingSlash(String path) {
        return path.endsWith("/") ? path : path + "/";
    }


}