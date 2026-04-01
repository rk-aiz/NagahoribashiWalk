package com.example.nagahoribashi_walk.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * usersテーブルに対応するEntity
 * @Author 海津
 */
@Data
public class User implements Serializable {

	// 主キー
	private Long id;
	
	// ユーザー名
	private String username;
	
	// ログイン認証に使用するパスワード
	private String password;
	
	// ユーザーのメールアドレス
	private String email;
	
	// ユーザーの権限区分を表す項目
	private String role;
	
	// 画面上に表示するユーザー名
	private String displayName;
	
	// 論理削除された日時を保持する項目
	private LocalDateTime deletedAt;
	
	// ユーザー情報の作成日時
	private LocalDateTime createdAt;
	
	// ユーザー情報の最終更新日時
	private LocalDateTime updatedAt;
	
	// ユーザーが利用可能状態かどうかを表すフラグ
	private boolean enabled;
}
