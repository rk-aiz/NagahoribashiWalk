package com.example.nagahoribashi_walk.dto;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * ユーザー管理画面用DTO
 *
 * @author 篠原
 */
@Data
public class AdminUserRow {

    /** 主キー */
    private Long id;

    /** ユーザー名 */
    private String username;

    /** 表示名 */
    private String displayName;

    /** メールアドレス */
    private String email;//

    /** 役割 */
    private String role;

    /** 有効フラグ */
    private Boolean enabled;

    /** 作成日時 */
    private LocalDateTime createdAt;

    /** 更新日時 */
    private LocalDateTime updatedAt;

    /** 削除日時 */
    private LocalDateTime deletedAt;
}
