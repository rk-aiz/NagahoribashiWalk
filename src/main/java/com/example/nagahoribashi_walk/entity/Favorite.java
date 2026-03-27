package com.example.nagahoribashi_walk.entity;

import java.time.LocalDateTime;

import lombok.Data;

/*
 * @author 正本
 */

@Data
public class Favorite {

	//主キー
	private Long id;

	//お気に入りしたユーザーのID(users.idへの参照)
	private Long userId;

	//お気に入りされたスポットのID(spots.idへの参照)
	private Long spotId;

	//お気に入り登録された日時
	private LocalDateTime createdAt;

}
