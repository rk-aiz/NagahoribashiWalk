package com.example.nagahoribashi_walk.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

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
     */
    public static String joinPath(String... segments) {

        if (segments == null || segments.length == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        Iterator<String> it = Arrays.stream(segments).filter(s -> s != null && !s.isBlank()).iterator();

        boolean isFirst = true;
        while (it.hasNext()) {
            String s = it.next();
            if (!isFirst && s.startsWith("/")) {
                sb.append(s.substring(1));
            } else {
                sb.append(s);
            }
            if (it.hasNext() && !s.endsWith("/")) {
                sb.append("/");
            }
            isFirst = false;
        }
        return sb.toString();
    }

    /**
     * 文字列が安全な内部パスかどうかを検証する。
     */
    public static boolean isInternalPath(String path) {
        return path != null && !path.isBlank()
                && path.startsWith("/") && !path.startsWith("//");
    }
}