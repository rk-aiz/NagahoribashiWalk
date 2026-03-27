package com.example.nagahoribashi_walk.entity;

//大谷作成

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Category {
	private Long id;
	private String name;
	private Integer displayOrder;

}
