package com.example.nagahoribashi_walk.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserProfile {

    /** ユーザー名 */
    private String username;

    /** メールアドレス */
    private String email;

    // ポイント
    private BigDecimal point;

    /** 表示名 */
    private String displayName;

    /** 登録日 */
    private LocalDateTime createdAt;

    public String getPointStr() {
        return point.toString();
    }
}
