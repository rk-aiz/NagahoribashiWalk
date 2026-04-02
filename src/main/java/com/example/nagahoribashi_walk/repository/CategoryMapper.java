package com.example.nagahoribashi_walk.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.nagahoribashi_walk.dto.NavCategory;

/**
 * categoriesテーブルに対応したMapperのインターフェース
 * 
 * @author 海津
 */
@Mapper
public interface CategoryMapper {

    List<NavCategory> findAll();
}
