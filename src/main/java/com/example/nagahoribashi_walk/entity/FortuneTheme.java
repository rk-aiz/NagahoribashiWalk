package com.example.nagahoribashi_walk.entity;

import lombok.Data;

/**
 * FortuneTheme Entity
 *
 * @author 海津
 */
@Data
public class FortuneTheme {

    /** 主キー */
    private Long id;

    /** おみくじを引く際の気分 */
    private String mood;

    /** 気分に対応したテーマ */
    private String keywords;

}
