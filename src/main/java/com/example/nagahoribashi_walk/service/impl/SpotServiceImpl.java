package com.example.nagahoribashi_walk.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagahoribashi_walk.dto.SpotSummary;
import com.example.nagahoribashi_walk.repository.SpotMapper;
import com.example.nagahoribashi_walk.service.SpotService;

import lombok.RequiredArgsConstructor;

/**
 * スポット関連サービスの実装クラス
 * 
 * @author 海津
 */
@Service
@Transactional
@RequiredArgsConstructor
public class SpotServiceImpl implements SpotService {

	private final SpotMapper spotMapper;

	/**
	 * ページに対応したスポット一覧を返す
	 */
	@Override
	public Page<SpotSummary> getPage(Pageable pageable) {

		// スポットの総数を取得する
		long total = spotMapper.count();

		// 対象ページに対応したスポットを取得する
		List<SpotSummary> spots = spotMapper.findAll(pageable.getOffset(), pageable.getPageSize());

		// Page<T>インスタンスに詰めて返す
		return new PageImpl<>(spots, pageable, total);
	}
	/**
	 * カタカナ → ひらがな変換
	 */
	private String toHiragana(String input) {
		if (input == null) return null;

		StringBuilder sb = new StringBuilder();
		for (char c : input.toCharArray()) {
			// カタカナ範囲ならひらがなへ変換
			if (c >= 'ァ' && c <= 'ン') {
				sb.append((char) (c - 0x60));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
	/**
	 * ひらがな → カタカナ変換
	 */
	private String toKatakana(String input) {
		if (input == null) return null;

		StringBuilder sb = new StringBuilder();
		for (char c : input.toCharArray()) {
			// ひらがな範囲ならカタカナへ変換
			if (c >= 'ぁ' && c <= 'ん') {
				sb.append((char) (c + 0x60));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
	/**
	 * ページとキーワードに対応したスポット一覧を返す
	 */
	@Override
	public Page<SpotSummary> searchByKeywords(String keyword, Pageable pageable) {

		// 空文字は一覧にフォールバック
		if (keyword == null || keyword.isBlank()) {
			return getPage(pageable);
		}

		// ひらがな・カタカナに変換
		String hiraganaKeyword = toHiragana(keyword);
		String katakanaKeyword = toKatakana(keyword);

		// 件数取得
		long total = spotMapper.countByKeywords(hiraganaKeyword, katakanaKeyword);

		System.out.println(total);
		
		// データ取得
		List<SpotSummary> spots =
				spotMapper.searchByKeywords(
						hiraganaKeyword,
						katakanaKeyword,
						pageable.getOffset(),
						pageable.getPageSize()
						);

		return new PageImpl<>(spots, pageable, total);
	}


	//トップページおすすめ３件表示用
	@Override
	public List<SpotSummary> getRecommendedSpots() {
		return spotMapper.findRecommendedSpots();
	}
}
