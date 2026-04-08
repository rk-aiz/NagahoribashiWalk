package com.example.nagahoribashi_walk.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * プロフィール編集フォーム
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileEditForm {

    /** 表示名 */
    @NotBlank(message = "表示名を入力してください")
    @Size(max = 50, message = "表示名は50文字以内で入力してください")
    private String displayName;

    /** メールアドレス */
    @NotBlank(message = "メールアドレスを入力してください")
    @Email(message = "正しいメールアドレスの形式で入力してください")
    private String email;

}
