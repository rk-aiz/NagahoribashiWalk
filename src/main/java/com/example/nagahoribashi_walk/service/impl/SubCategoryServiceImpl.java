package com.example.nagahoribashi_walk.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagahoribashi_walk.dto.SubCategoryDTO;
import com.example.nagahoribashi_walk.entity.SubCategory;
import com.example.nagahoribashi_walk.repository.SubCategoryMapper;
import com.example.nagahoribashi_walk.service.SubCategoryService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SubCategoryServiceImpl implements SubCategoryService {

    private final SubCategoryMapper subCategoryMapper;
    private final SubCategoryDTO SUBCATEGORY_OTHER = new SubCategoryDTO(null, "その他");

    @Override
    public SubCategoryDTO getById(Long subCategoryId) {
        return subCategoryMapper.findById(subCategoryId)
                .orElse(SUBCATEGORY_OTHER);
    }

}
