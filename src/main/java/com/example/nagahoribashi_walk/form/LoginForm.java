package com.example.nagahoribashi_walk.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginForm {

    /** ユーザー名 */
    private String usernameInput;

    /** パスワード */
    private String passwordInput;
}
