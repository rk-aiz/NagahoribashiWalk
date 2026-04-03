package com.example.nagahoribashi_walk.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AdminUserRow {

	private Long id;//主キー

	private String username;//ユーザー名

	private String displayName;//表示名

	private String email;//メールアドレス

	private String role;//役割

	private LocalDateTime createdAt;//作成日時

	private LocalDateTime updatedAt;//更新日時

	private Boolean enabled;//有効フラグ
}
