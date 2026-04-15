package com.example.nagahoribashi_walk.entity;

import lombok.Data;

/**
 * おみくじテーマのエンティティ
 *
 * @author 海津
 */
@Data
public class FortuneTheme {

    // 主キー
    private Long id;

    // 気分
    private String mood;

    // キーワード
    private String keywords;

}
