package com.example.nagahoribashi_walk.dto;

import java.time.LocalDateTime;

import lombok.Data;

/** 
 * @author 池田
 */
@Data
public class ReviewDTO {
	
	//レビュー内容
	//r.comment
	private String comment;
	
	//レビュー投稿者名
	//u.display_name
	private String reviewerName;
	
	//レビュー投稿日
	//r.created_at
	private LocalDateTime reviewDate;

    // 5段階評価
    private Integer rating;

}
