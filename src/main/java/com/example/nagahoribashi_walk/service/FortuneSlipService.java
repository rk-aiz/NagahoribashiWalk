package com.example.nagahoribashi_walk.service;

import java.util.Map;

import com.example.nagahoribashi_walk.dto.FortuneResult;
import com.example.nagahoribashi_walk.service.userdetails.LoginUser;

/**
 * おみくじ関連サービス
 *
 * @author 海津
 */
public interface FortuneSlipService {

    /** 気分の選択肢を取得 */
    Map<Long, String> getMoodSelection(LoginUser user);

    /** ウィンドウで既におみくじを引いているか確認 */
    boolean isAlreadyDrawn(LoginUser user);

    /** おみくじを引く */
    void draw(Long themeId, LoginUser loginUser);

    /** おみくじ結果を取得 */
    FortuneResult getFortuneResult(LoginUser loginUser);

}
