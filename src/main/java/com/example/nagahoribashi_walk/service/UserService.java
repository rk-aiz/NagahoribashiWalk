package com.example.nagahoribashi_walk.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.nagahoribashi_walk.entity.User;

/**
 * ユーザー関連のサービスのインターフェース
 * @author 海津
 */
public interface UserService {

    /**
     * ユーザー名から、UserProfileインスタンスを取得する
     */
	//UserProfile getProfileByUsername(String username);
	
    /**
     * Userを新規保存する
     */
	void register(User user);

    /**
     * プロフィールを更新する
     */
	void updateProfile(User user);

    /**
     * ユーザーの削除を行う
     */
	void delete(Long id);
	
    /**
     * ユーザーの有効・無効を切り替える
     */
	void toggleEnabled(Long id);
	
    /**
     * ページネーション付きでユーザー一覧を取得する
     */
	Page<User> getPage(Pageable pageable);

}
