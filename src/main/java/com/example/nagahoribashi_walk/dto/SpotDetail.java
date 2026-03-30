package com.example.nagahoribashi_walk.dto;

import java.util.List;

import lombok.Data;

/** 
 * @author 池田
 */

@Data
public class SpotDetail {
	
	//スポットID
	//s.id
	private Long id;
	
	//店舗名
	//s.spot_name
	private String spotName;
	
	//平均評価。レビュー0件時は null
	//AVG(r.rating)
	private Double averageRating;
	
	//スポット詳細説明
	//s.details
	private String details;
	
	//営業時間
	//s.business_hours
	private String businessHours;
	
	//予算目安
	//s.estimated_budget
	private String estimatedBudget;
	
	//総レビュー件数。レビューがない場合は 0
	//COUNT(r.id)
	private Integer reviewCount;
	
	//レビュー一覧。子DTOとして保持
	private List<ReviewDTO> reviews;

}
