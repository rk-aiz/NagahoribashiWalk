package com.example.nagahoribashi_walk.dto;

import java.util.List;

/**
 * スポット一覧ページのサイドバー用DTO
 */
public record SidebarDTO(
        List<NavCategory> categories,
        Long selectedCategoryId,
        Long selectedSubCategoryId) {
}
