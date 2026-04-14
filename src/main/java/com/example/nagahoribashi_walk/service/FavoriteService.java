package com.example.nagahoribashi_walk.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.nagahoribashi_walk.dto.FavoriteSummary;

/**
 * お気に入り関連サービスのインターフェース
 * 
 * @author 正本
 */
public interface FavoriteService {

	/** お気に入り一覧をページネーションで取得 */
	Page<FavoriteSummary> getPage(Long userId, Pageable pageable);

	/** お気に入り登録。ポイントが変動した場合はその差分を返す（変動なしは0） */
	int addFavorite(Long userId, Long spotId);

	/** お気に入り削除。ポイントが変動した場合はその差分を返す（変動なしは0） */
	int removeFavorite(Long userId, Long spotId);

	/** お気に入り存在確認 */
	boolean isFavorite(Long userid, Long spotId);
}
