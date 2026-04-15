package com.example.nagahoribashi_walk.dto;

import java.util.List;

/**
 * スポット一覧ページのサイドバー用DTO (record)
 *
 * @author 海津
 */
public record SidebarDTO(

        // カテゴリの一覧
        List<NavCategory> categories,

        // 選択中のカテゴリに属するサブカテゴリの一覧
        List<NavSubCategory> subCategories,

        // 選択中のカテゴリのID
        Long selectedCategoryId,

        // 選択中のサブカテゴリのID
        Long selectedSubCategoryId) {
}
