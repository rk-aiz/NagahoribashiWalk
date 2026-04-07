package com.example.nagahoribashi_walk.repository;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.nagahoribashi_walk.dto.AdminUserRow;
import com.example.nagahoribashi_walk.dto.UserProfile;
import com.example.nagahoribashi_walk.entity.User;

/**
 * usersテーブルに対応したMapperのインターフェース
 * 
 * @author 海津
 */
@Mapper
public interface UserMapper {

    /**
     * ユーザー名から、Userインスタンスを取得する
     */
    Optional<User> findByUsername(String username);

    /**
     * ユーザー名から、UserProfileインスタンスを取得する
     */
    Optional<UserProfile> findProfileByUsername(String username);

    /**
     * Userを新規保存する
     */
    void insert(User user);

    /**
     * ソフトデリートを行う
     */
    void softDelete(Long id);

    /**
     * ユーザーの有効・無効を切り替える
     */
    void toggleEnabled(Long id);

    /**
     * ページネーション付きでユーザー一覧を取得する
     */
    List<User> findAll(@Param("offset") long offset, @Param("limit") int limit);

    /**
     * ユーザー数をカウントする
     */
    long count();

    /**
     * ユーザーの存在チェック
     */
    boolean exists(@Param("username") String username);

    /**
     * メールアドレス存在チェック
     */
    boolean existsByEmail(@Param("email") String email);

    /**
     * 管理者側ユーザー一覧
     */
    List<AdminUserRow> findAllForAdmin(
            @Param("limit") int limit,
            @Param("offset") long offset,
            @Param("sort") String sort);

    long countAdminUsers();
    
    /**
     * 管理者側ユーザー一覧検索用
     */
    List<AdminUserRow> searchAdminUsers(String keyword, int limit, long offset, String sort);

    long countSearchAdminUsers(String keyword);

}
