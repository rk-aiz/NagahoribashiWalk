package com.example.nagahoribashi_walk.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.nagahoribashi_walk.entity.User;

@Mapper
public interface UserMapper {

	User findByUsername(String username);
	
	void insert(User user);
	
	void softDelete(Long id);
	
	void toggleEnabled(Long id);
	
	List<User> findAllForAdmin();
}
