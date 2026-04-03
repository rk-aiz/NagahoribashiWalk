package com.example.nagahoribashi_walk.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagahoribashi_walk.dto.NavCategory;
import com.example.nagahoribashi_walk.entity.Category;
import com.example.nagahoribashi_walk.repository.CategoryMapper;
import com.example.nagahoribashi_walk.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

	private final CategoryMapper categoryMapper;

	@Override
	public List<NavCategory> findAll() {
		return categoryMapper.findAll();
	}

	//追加
	@Override
    public void insertCategory(Category category) {
    	categoryMapper.insertCategory(category);
    }

	//削除
	@Override
    public void deleteCategory(Long id) {
	categoryMapper.deleteCategory(id);
    }
}
