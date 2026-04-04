package com.example.nagahoribashi_walk.service.impl;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagahoribashi_walk.dto.AdminUserRow;
import com.example.nagahoribashi_walk.dto.UserProfile;
import com.example.nagahoribashi_walk.entity.User;
import com.example.nagahoribashi_walk.exception.UserAlreadyExistsException;
import com.example.nagahoribashi_walk.repository.UserMapper;
import com.example.nagahoribashi_walk.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * ユーザー関連サービスの実装クラス
 * 
 * @author 海津
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserProfile getProfileByUsername(String username) {
        return userMapper.findProfileByUsername(username).orElseThrow(() -> {
            throw new UsernameNotFoundException(
                    "ユーザーが存在しません");
        });
    }

    @Override
    public void register(User user, String rowPassword) {

        // ユーザー名が既に存在するか確認
        if (userMapper.exists(user.getUsername())) {
            throw new UserAlreadyExistsException("ユーザー名はすでに存在します。");
        }

        // メールアドレスが既に存在するか確認
        if (userMapper.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("すでに登録されたメールアドレスです。");
        }

        // パスワードをハッシュ化
        user.setPassword(passwordEncoder.encode(rowPassword));

        try {
            userMapper.insert(user);
        } catch (Exception e) {
            throw new DataIntegrityViolationException(e.getLocalizedMessage());
        }
    }

    @Override
    public void updateProfile(User user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateProfile'");
    }

    @Override
    public void toggleEnabled(Long id) {

        // ユーザー取得
        userMapper.toggleEnabled(id);
    }

    @Override
    public Page<AdminUserRow> getAdminUserPage(Pageable pageable, String sort) {

        long offset = (int) pageable.getOffset();
        int pageSize = pageable.getPageSize();

        List<AdminUserRow> list = userMapper.findAllForAdmin(pageSize, offset, sort);

        long total = userMapper.countAdminUsers();

        return new PageImpl<>(list, pageable, total);
    }

    public void delete(String username, String loginUsername) {
        // ★ 自分削除禁止
        if (username.equals(loginUsername)) {
            throw new IllegalStateException("自分自身は削除できません");
        }

        User user = userMapper.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        // ★ 論理削除
        userMapper.softDelete(user.getId());
    }

    @Override
    public Page<User> getPage(Pageable pageable) {

        long total = userMapper.count();

        // 対象ページに対応したスポットを取得する
        List<User> users = userMapper.findAll(pageable.getOffset(), pageable.getPageSize());

        // Page<T>インスタンスに詰めて返す
        return new PageImpl<>(users, pageable, total);
    }
}