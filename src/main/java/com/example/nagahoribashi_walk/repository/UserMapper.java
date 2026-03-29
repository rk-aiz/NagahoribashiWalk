package com.example.nagahoribashi_walk.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
    User findByUsername(String username);

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
    List<User> findPage(@Param("offset") long offset, @Param("limit") int limit);

    /**
     * ユーザー数をカウントする
     */
    long count();

    /**
     * ユーザーの存在チェック
     */
    boolean exists(@Param("username") String username);
}
