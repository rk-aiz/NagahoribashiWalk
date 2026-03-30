package com.example.nagahoribashi_walk.dto;

import lombok.Data;

/** 
 * @author 池田
 */
@Data
public class SpotPhotoDTO	 {
	
	//画像ID
	//sp.id
	private Long id;
	
	//写真のURL
	//sp.photo_url
	private String photoUrl;
	
	//写真の表示順
	//sp.display_order
	private Integer displayOrder;

}
