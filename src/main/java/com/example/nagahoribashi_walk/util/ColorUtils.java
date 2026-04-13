package com.example.nagahoribashi_walk.util;

import java.util.concurrent.ThreadLocalRandom;

public class ColorUtils {

    private ColorUtils() {}

    /**
     * 白背景上でテキストとして読みやすいランダムな色を生成する。
     * 彩度・明度を固定し、色相のみをランダムにすることでコントラストを確保する。
     *
     * @return "#rrggbb" 形式の文字列
     */
    public static String generateReadableColor() {
        ThreadLocalRandom tlr = ThreadLocalRandom.current();
        int hue = tlr.nextInt(360);                          // 色相: 0〜359
        double saturation = 0.60 + tlr.nextDouble() * 0.15; // 彩度: 60〜75%
        double lightness  = 0.38 + tlr.nextDouble() * 0.12; // 明度: 38〜50%
        return hslToHex(hue, saturation, lightness);
    }

    /**
     * HSL値を16進数カラーコード（例: #ec4899）に変換する
     *
     * @param hue        色相 (0〜359)
     * @param saturation 彩度 (0.0〜1.0)
     * @param lightness  明度 (0.0〜1.0)
     * @return "#rrggbb" 形式の文字列
     */
    public static String hslToHex(int hue, double saturation, double lightness) {
        double c = (1 - Math.abs(2 * lightness - 1)) * saturation;
        double x = c * (1 - Math.abs((hue / 60.0) % 2 - 1));
        double m = lightness - c / 2;
        double r, g, b;
        if (hue < 60) {
            r = c; g = x; b = 0;
        } else if (hue < 120) {
            r = x; g = c; b = 0;
        } else if (hue < 180) {
            r = 0; g = c; b = x;
        } else if (hue < 240) {
            r = 0; g = x; b = c;
        } else if (hue < 300) {
            r = x; g = 0; b = c;
        } else {
            r = c; g = 0; b = x;
        }
        int ri = (int) Math.round((r + m) * 255);
        int gi = (int) Math.round((g + m) * 255);
        int bi = (int) Math.round((b + m) * 255);
        return String.format("#%02x%02x%02x", ri, gi, bi);
    }
}
