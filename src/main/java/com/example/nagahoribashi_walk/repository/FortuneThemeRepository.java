package com.example.nagahoribashi_walk.repository;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.example.nagahoribashi_walk.entity.FortuneTheme;

/**
 * 「気分とキーワードの組み合わせ」のリポジトリ
 *
 * @author 海津
 */
public interface FortuneThemeRepository {

    List<FortuneTheme> findAll();

    Optional<FortuneTheme> findById(Long id);

    void save(FortuneTheme fortuneTheme);

    void insert(String mood, String keywords);

    List<FortuneTheme> findRandom(int limit, Random random);

}
