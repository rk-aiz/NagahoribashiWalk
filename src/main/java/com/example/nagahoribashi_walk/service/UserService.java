package com.example.nagahoribashi_walk.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.nagahoribashi_walk.dto.AdminUserRow;
import com.example.nagahoribashi_walk.dto.UserProfile;
import com.example.nagahoribashi_walk.entity.User;

/**
 * ユーザー関連のサービスのインターフェース
 * 
 * @author 海津
 */
public interface UserService {

    /**
     * ユーザー名から、UserProfileインスタンスを取得する
     */
    UserProfile getProfileByUsername(String username);

    /**
     * Userを新規保存する
     */
    void register(User user, String rawPassword);

    /**
     * プロフィールを更新する
     */
    void updateProfile(User user);
    
    /**
     * ユーザー自身が退会する用
     */
    void unsubscribe(Long userId);
    

    /**
     * (管理者用)ユーザーの削除を行う
     */
    void delete(String userName, String loginUsername);

    /**
     * (管理者用)ユーザーの有効・無効を切り替える
     */
    void toggleEnabled(Long id);
    
    Page<User> getPage(Pageable pageable);
    

    /**
     * ページネーション付きで管理者側がユーザー一覧を取得する
     */
    Page<AdminUserRow> getAdminUserPage(Pageable pageable,String sort,String keyword,boolean incluedDeleted);
}
