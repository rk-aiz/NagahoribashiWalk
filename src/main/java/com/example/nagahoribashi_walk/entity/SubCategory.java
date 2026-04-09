package com.example.nagahoribashi_walk.entity;

import lombok.Data;

/**
 * @author 篠原
 */
@Data
public class SubCategory {

	//主キー
	private Long id;
	
	//親となるカテゴリのID（categories.id 参照）
	private Long categoryId;
	
	//サブカテゴリ名
	private String name;

	public int getDisplayOrder() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}
}
