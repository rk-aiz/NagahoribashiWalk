package com.example.nagahoribashi_walk.repository;

import org.apache.ibatis.annotations.Mapper;

/**
 * favoritesテーブルに対応したMapperのインターフェース
 * 
 */
@Mapper
public interface FavoriteMapper {

    long count();
}
