package com.example.nagahoribashi_walk.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.hibernate.validator.constraints.URL;

import lombok.Data;

/**
 * Spot登録・編集用のForm
 * 
 * @author 海津
 */
@Data
public class SpotForm {

    // 新規登録か編集(更新)を判定する
    private boolean isNew;

    // スポットの正式名称。画面上のタイトルとして使用
    @NotBlank(message = "スポット名を入力してください")
    @Size(max = 255, message = "スポット名は255文字以内で入力してください")
    private String spotName;

    // sub_categoriesテーブルのID。未分類の場合はNULLを許容
    @NotNull(message = "カテゴリを選択してください")
    private Long subCategoryId;

    // 公式サイトのURL。https://から始まる文字列
    @URL(message = "正しいURLを入力してください")
    private String websiteUrl;

    // Googleマップの共有URL。埋め込み表示などに使用
    @URL(message = "正しいURLを入力してください")
    private String gmapUrl;

    // スポットの所在地
    private String address;

    // 営業時間。自由テキスト入力
    private String businessHours;

    // 定休日。テキスト形式
    private String closedDays;

    // 予算の目安。「1,000円〜2,000円」のような表示用
    private String estimatedBudget;

    // 検索用キーワード
    private String keywords;

    // スポットの詳細説明
    private String details;

    // Thymeleafが正しくbooleanにアクセスする用
    public boolean getIsNew() {
        return this.isNew;
    }
}
