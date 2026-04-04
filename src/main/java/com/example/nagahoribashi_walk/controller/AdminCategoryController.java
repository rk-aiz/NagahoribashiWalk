package com.example.nagahoribashi_walk.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.nagahoribashi_walk.dto.AdminCategoryRow;
import com.example.nagahoribashi_walk.dto.AdminSubCategoryRow;

/**
 * 管理者用カテゴリ管理コントローラー
 *
 * TODO: 現在はスタブ実装。実装ではCategoryServiceを使用
 */
@Controller
public class AdminCategoryController {

    @GetMapping("/admin/category/list")
    public String list(Model model) {

        // ---- スタブデータ ----
        AdminCategoryRow gourmet = makeCategory(1L, "グルメ", 1, List.of(
                makeSub(1L, "居酒屋", 1L, false),
                makeSub(2L, "外国料理屋", 1L, false),
                makeSub(3L, "ラーメン", 1L, false),
                makeSub(4L, "カレー", 1L, false),
                makeSub(5L, "その他", 1L, true) // デフォルト（isDefault=true 相当）
        ));

        AdminCategoryRow tourism = makeCategory(2L, "観光スポット", 2, List.of(
                makeSub(6L, "神社", 2L, false),
                makeSub(7L, "劇場", 2L, false),
                makeSub(8L, "その他", 2L, true)));

        AdminCategoryRow shopping = makeCategory(3L, "ショッピング", 3, List.of(
                makeSub(9L, "ガチャ", 3L, false),
                makeSub(10L, "その他", 3L, true)));

        AdminCategoryRow entertainment = makeCategory(4L, "娯楽", 4, List.of(
                makeSub(11L, "ライブハウス", 4L, false),
                makeSub(12L, "その他", 4L, true)));

        AdminCategoryRow cafe = makeCategory(5L, "カフェ", 5, List.of(
                makeSub(13L, "カフェ", 5L, false),
                makeSub(14L, "その他", 5L, true)));

        AdminCategoryRow other = makeCategory(5L, "その他", 5, List.of(
                makeSub(13L, "自販機", 5L, false),
                makeSub(14L, "その他", 5L, true)));
        other.setDefault(true);

        model.addAttribute("categories", List.of(gourmet, tourism, shopping, entertainment, cafe, other));
        return "admin/category/list";
    }

    private AdminCategoryRow makeCategory(Long id, String name, int order, List<AdminSubCategoryRow> subs) {
        AdminCategoryRow c = new AdminCategoryRow();
        c.setId(id);
        c.setName(name);
        c.setDisplayOrder(order);
        c.setSubCategories(subs);
        return c;
    }

    private AdminSubCategoryRow makeSub(Long id, String name, Long categoryId, boolean isDefault) {
        AdminSubCategoryRow s = new AdminSubCategoryRow();
        s.setId(id);
        s.setName(name);
        s.setCategoryId(categoryId);
        s.setDefault(isDefault);
        return s;
    }
}
