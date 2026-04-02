package com.example.nagahoribashi_walk.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagahoribashi_walk.entity.SubCategory;
import com.example.nagahoribashi_walk.repository.SubCategoryMapper;
import com.example.nagahoribashi_walk.service.SubCategoryService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SubCategoryServiceImpl implements SubCategoryService {

    private final SubCategoryMapper subCategoryMapper;

    @Override
    public Map<Long, List<SubCategory>> findAllGroupedByCategory() {
        return subCategoryMapper.findAll().stream()
                .collect(Collectors.groupingBy(SubCategory::getCategoryId));
    }
}
