package com.example.nagahoribashi_walk.service.impl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
 * @author 海津, 篠原
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    /** ユーザー名から、UserProfileインスタンスを取得する */
    @Override
    public UserProfile getProfileByUsername(String username) {
        return userMapper.findProfileByUsername(username).orElseThrow(() -> {
            throw new UsernameNotFoundException(
                    "ユーザーが存在しません");
        });
    }

    /** Userを新規保存する */
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

    /** プロフィールを更新する */
    @Override
    public void updateProfile(User user) {
        // 更新対象のユーザーを取得
        User existingUser = userMapper.findByUsername(user.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("ユーザーが存在しません"));

        // メールアドレスを変更する場合、他ユーザーと重複していないか確認
        if (!existingUser.getEmail().equals(user.getEmail())
                && userMapper.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("すでに登録されたメールアドレスです。");
        }

        // 更新対象に現在の値を反映
        existingUser.setEmail(user.getEmail());
        existingUser.setDisplayName(user.getDisplayName());

        // 更新処理を実行
        userMapper.updateProfile(existingUser);
    }

    /** ユーザー自身が退会する用 */
    @Override
    public void unsubscribe(Long userId) {
        User user = userMapper.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("id: " + userId));
        String prefix = "#del_" + LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS) + "#";
        userMapper.softDelete(userId, prefix + user.getUsername(), prefix + user.getEmail());
    }

    /** (管理者用)ユーザーの有効・無効を切り替える */
    @Override
    public void toggleEnabled(Long id) {

        // ユーザー取得
        userMapper.toggleEnabled(id);
    }

    /** (管理者用)ユーザー一覧を取得する */
    @Override
    public Page<AdminUserRow> getAdminUserPage(Pageable pageable, String sort, String keyword, boolean includeDeleted) {

        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();
        String sortLowerCase = sort.toLowerCase();

        List<AdminUserRow> list;
        long total;

        if (keyword == null || keyword.isBlank()) {
            // 通常一覧
            list = userMapper.findAllForAdmin(pageSize, offset, sortLowerCase, includeDeleted);
            total = userMapper.countAdminUsers(includeDeleted);
        } else {
            // 検索あり
            list = userMapper.searchForAdminByKeyword(keyword, pageSize, offset, sortLowerCase, includeDeleted);
            total = userMapper.countForAdminByKeyword(keyword, includeDeleted);
        }

        return new PageImpl<>(list, pageable, total);
    }

    /** ユーザーを論理削除する */
    @Override
    public void delete(String username, String loginUsername) {
        // ★ 自分削除禁止
        if (username.equals(loginUsername)) {
            throw new IllegalStateException("自分自身は削除できません");
        }

        User user = userMapper.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        // ★ 論理削除
        String prefix = "#del_" + LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS) + "#";
        userMapper.softDelete(user.getId(), prefix + user.getUsername(), prefix + user.getEmail());
    }

    /** Role対象ユーザー数をカウントする */
    @Override
    public long getUserCountByRole(String role) {
        return userMapper.countByRole(role);
    }
}