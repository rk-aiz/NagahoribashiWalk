package com.example.nagahoribashi_walk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * IDから名前を解決するためのDTO
 *
 * @author 海津
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryDTO {

    private Long id;

    private String name;
}
