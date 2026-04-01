package com.example.nagahoribashi_walk.service.impl;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProfileByUsername'");
    }

    @Override
    public void register(User user, String rowPassword) {
        
    	// ユーザー名が既に存在するか確認
    	if (userMapper.exists(user.getUsername())) {
    		throw new UserAlreadyExistsException("ユーザー名はすでに存在します。");
    	}
    	
    	/** Emailを一意にする
    	if (userMapper.findByEmail(user.getUsername()).isPresent()) {
    		throw new UserAlreadyExistsException("すでに登録されたメールアドレスです。");
    	}
    	*/
    	
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
    public void delete(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public void toggleEnabled(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toggleEnabled'");
    }

    @Override
    public Page<User> getPage(Pageable pageable) {
        long total = userMapper.count();
        List<User> users = userMapper.findAll(pageable.getOffset(), pageable.getPageSize());
        return new PageImpl<>(users, pageable, total);
    }
    
    @Override
    public Page<AdminUserRow> getAdminUserPage(Pageable pageable) {
        return userMapper.findAllForAdmin(pageable);
    }
}
