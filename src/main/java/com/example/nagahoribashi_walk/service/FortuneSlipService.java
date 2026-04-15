package com.example.nagahoribashi_walk.service;

import java.time.LocalDateTime;
import java.util.Map;

import com.example.nagahoribashi_walk.dto.FortuneResult;
import com.example.nagahoribashi_walk.service.userdetails.LoginUser;

/**
 * おみくじ関連サービス
 *
 * @author 海津
 */
public interface FortuneSlipService {

    Map<Long, String> getMoodSelection(LoginUser user);

    boolean isAlreadyDrawn(LoginUser user);

    void draw(Long themeId, LoginUser loginUser);

    FortuneResult getFortuneResult(LoginUser loginUser);

    LocalDateTime getNextDrawTime(LoginUser loginUser);

}
