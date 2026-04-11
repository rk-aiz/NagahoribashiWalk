package com.example.nagahoribashi_walk.util;

import java.util.Arrays;
import java.util.Iterator;

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

    /**
     * ひらがな・カタカナのコードポイント差分。
     *
     * Unicode においてひらがな（U+3041〜U+3094）とカタカナ（U+30A1〜U+30F4）は
     * 同じ並び順で配置されており、対応する文字同士の差は常に 0x60（= 96）で一定。
     * この値は Unicode 仕様で固定されているため、定数として定義して使用する。
     */
    private static final int KANA_OFFSET = 'ア' - 'あ'; // 0x60

    /**
     * カタカナ → ひらがな変換。
     */
    public static String toHiragana(String input) {
        if (input == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {

            // 変換範囲: 'ァ'(U+30A1) 〜 'ヴ'(U+30F4)
            // 末尾を 'ン'(U+30F3) ではなく 'ヴ'(U+30F4) まで広げているのは、
            // 対応するひらがな 'ゔ'(U+3094) が存在するため。
            // 'ヴ' は 'ン' の次のコードポイントなので、'ァ'〜'ン' の範囲では変換されない。
            if (c >= 'ァ' && c <= 'ヴ') {
                sb.append((char) (c - KANA_OFFSET));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * ひらがな → カタカナ変換。
     */
    public static String toKatakana(String input) {

        if (input == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {

            // 変換範囲: 'ァ'(U+30A1) 〜 'ヴ'(U+30F4)
            // 末尾を 'ン'(U+30F3) ではなく 'ヴ'(U+30F4) まで広げているのは、
            // 対応するひらがな 'ゔ'(U+3094) が存在するため。
            // 'ヴ' は 'ン' の次のコードポイントなので、'ァ'〜'ン' の範囲では変換されない。
            if (c >= 'ぁ' && c <= 'ゔ') {
                sb.append((char) (c + KANA_OFFSET));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}