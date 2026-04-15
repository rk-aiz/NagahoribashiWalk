package com.example.nagahoribashi_walk.form;

import java.util.Objects;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 新規ユーザー登録用のFormオブジェクト
 *
 * @author 海津
 */
@Data
public class UserRegisterForm {

    // ユーザー名（ログインID）。半角英数字とアンダースコアのみ
    @NotBlank(message = "ユーザー名を入力してください")
    @Size(min = 3, max = 30, message = "ユーザー名は3〜30文字で入力してください")
    @Pattern(regexp = "^\\w+$", message = "ユーザー名は半角英数字とアンダースコアのみ使用できます")
    private String username;

    // 表示名（画面上に表示される名前）
    @NotBlank(message = "表示名を入力してください")
    @Size(max = 50, message = "表示名は50文字以内で入力してください")
    private String displayName;

    // メールアドレス
    @NotBlank(message = "メールアドレスを入力してください")
    @Email(message = "正しいメールアドレスの形式で入力してください")
    @Size(max = 200, message = "メールアドレスは200文字以内まで対応しています")
    private String email;

    // パスワード（BCryptでハッシュ化して保存）
    @NotBlank(message = "パスワードを入力してください")
    @Size(min = 8, max = 100, message = "パスワードは8文字以上で入力してください")
    private String password;

    // パスワード確認用（DBには保存しない）
    @NotBlank(message = "パスワード（確認）を入力してください")
    private String passwordConfirm;

    @AssertTrue(message = "確認用パスワードが一致しません")
    public boolean isSamePassword() {
        return Objects.equals(password, passwordConfirm);
    }
}
