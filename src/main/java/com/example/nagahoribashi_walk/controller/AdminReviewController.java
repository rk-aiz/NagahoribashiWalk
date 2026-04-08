package com.example.nagahoribashi_walk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.nagahoribashi_walk.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminReviewController {

    private final ReviewService reviewService;

    @GetMapping("/admin/review/list")
    public String list() {
        // カラム: ID / スポット名 / 投稿者 / 評価 / コメント / 投稿日時 / 操作（削除ボタン）
        // 削除は POST /admin/review/delete/{id} + confirm() ダイアログ
        // コントローラー側で必要になる変数:

        // 変数 型 用途
        // reviewPages Page<?> テーブル + ページネーション
        // keyword String ページネーションのクエリ引き継ぎ（null でも可）
        // エンティティのフィールド名（spotName, userName, rating, comment,
        // createdAt）は実際のクラスに合わせて調整が必要です。
        return "admin/review/list";
    }

}
