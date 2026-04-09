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
 * @author 海津, 篠原, 池田
 */
@Mapper
public interface UserMapper {

    /**
     * ユーザー名から、Userインスタンスを取得する
     */
    Optional<User> findByUsername(@Param("username") String username);

    /**
     * ユーザー名から、UserProfileインスタンスを取得する
     */
    Optional<UserProfile> findProfileByUsername(@Param("username") String username);

    /**
     * Userを新規保存する
     */
    void insert(User user);

    /**
     * @author 池田
     *         プロフィールを更新する
     */
    void updateProfile(User user);

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
            @Param("sort") String sort,
            @Param("includeDeleted") boolean includeDeleted);

    long countAdminUsers(@Param("includeDeleted") boolean includeDeleted);

    /**
     * 管理者側ユーザー一覧検索用（ページネーション）
     */
    List<AdminUserRow> searchForAdminByKeyword(
            @Param("keyword") String keyword,
            @Param("limit") int limit,
            @Param("offset") long offset,
            @Param("sort") String sort,
            @Param("includeDeleted") boolean includeDeleted);

    /**
     * 管理者側ユーザー一覧検索用（総数取得）
     */
    long countForAdminByKeyword(@Param("keyword") String keyword, @Param("includeDeleted") boolean includeDeleted);

    /**
     * 対象Roleのユーザー数を取得
     */
    long countByRole(@Param("role") String role);

}
