package com.example.nagahoribashi_walk.dto;

import java.util.List;

import com.example.nagahoribashi_walk.entity.SubCategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * カテゴリ用DTO
 *
 * @author 海津
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {

    /** 主キー */
    private long id;

    /** カテゴリ名 */
    private String name;

    /** 表示順 */
    private int displayOrder;
}
