package com.example.nagahoribashi_walk.util;

/**
 * 文字列関連のユーティリティクラス
 *
 * @author 海津
 */
public class MyStringUtils {

    private MyStringUtils() {
    }

    /**
     * 文字列の末尾に"/"かあるか確認し、なければ付与する
     */
    public static String ensureTrailingSlash(String path) {
        return path.endsWith("/") ? path : path + "/";
    }

    /**
     * 複数のパス文字列を"/"で結合する。
     * 各セグメント間の重複する"/"は除去する。
     */
    public static String joinPath(String... segments) {
        if (segments == null || segments.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String segment : segments) {
            if (segment == null || segment.isEmpty()) {
                continue;
            }
            if (sb.length() > 0) {
                String trimmed = segment.startsWith("/") ? segment.substring(1) : segment;
                if (!sb.toString().endsWith("/")) {
                    sb.append("/");
                }
                sb.append(trimmed);
            } else {
                sb.append(segment);
            }
        }
        return sb.toString();
    }
}