package com.example.nagahoribashi_walk.repository.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Repository;

import com.example.nagahoribashi_walk.entity.FortuneTheme;
import com.example.nagahoribashi_walk.repository.FortuneThemeRepository;

/**
 * 「気分とキーワードの組み合わせ」のリポジトリの実装
 *
 * @author 海津
 */
@Repository
public class FortuneThemeRepositoryImpl implements FortuneThemeRepository {

    private final Map<Long, FortuneTheme> themes = new HashMap<>();

    public FortuneThemeRepositoryImpl() {
        initialize();
    }

    @Override
    public List<FortuneTheme> findAll() {
        return List.copyOf(themes.values());
    }

    @Override
    public Optional<FortuneTheme> findById(Long id) {
        return Optional.ofNullable(themes.get(id));
    }

    @Override
    public List<FortuneTheme> findRandom(int limit, Random random) {
        return themes.values().stream()
                .map(t -> Map.entry(random.nextInt(), t)) // 乱数キーを付与
                .sorted(Map.Entry.comparingByKey()) // キーでソート
                .limit(limit) // 先頭N件
                .map(Map.Entry::getValue) // FortuneThemeだけ取り出す
                .toList();
    }

    @Override
    public void save(FortuneTheme theme) {
        if (theme.getId() == null) {
            Long nextId = themes.isEmpty() ? 1L : Collections.max(themes.keySet()) + 1;
            theme.setId(nextId);
        }
        themes.put(theme.getId(), theme);
    }

    @Override
    public void insert(String mood, String keywords) {
        FortuneTheme theme = new FortuneTheme();
        Long nextId = themes.isEmpty() ? 1L : Collections.max(themes.keySet()) + 1;
        theme.setId(nextId);
        theme.setMood(mood);
        theme.setKeywords(keywords);
        themes.put(theme.getId(), theme);
    }

    private void initialize() {
        insert("のんびり癒されたい", "癒し,静か,リラックス,サウナ,整う");
        insert("がっつり食べたい", "グルメ,行列,人気店,焼肉,居酒屋");
        insert("カフェでひと息つきたい", "カフェ,スイーツ,おしゃれ,韓国カフェ,タピオカ");
        insert("パワーをもらいたい", "パワースポット,神社,ご利益,縁結び,厄除け");
        insert("思いっきり笑いたい", "お笑い,劇場,吉本,漫才,新喜劇");
        insert("ライブ・音楽に浸りたい", "ライブハウス,音楽,コンサート,ライブ,バンド");
        insert("食べ歩きを楽しみたい", "食べ歩き,たこ焼き,テイクアウト,千日前");
        insert("異国の味を楽しみたい", "外国料理,エスニック,本格,タイ料理,韓国料理");
        insert("映える写真を撮りたい", "インスタ映え,写真映え,おしゃれ,獅子殿");
        insert("ちょっと変わった体験がしたい", "ガチャガチャ,カプセルトイ,体験,室内スポット");
        insert("ラーメンが食べたい", "ラーメン,中華そば,麺,醤油ラーメン,あっさり");
        insert("辛いものが食べたい", "辛い,四川,カレー,ナン,エスニック");
        insert("焼肉でテンションを上げたい", "焼肉,和牛,タン,ハラミ,炭火");
        insert("お参りして運気を上げたい", "神社,お守り,ご利益,参拝,都会の中");
        insert("観劇・舞台を楽しみたい", "劇場,ミュージカル,観劇,舞台,演劇");
        insert("夜を思いっきり楽しみたい", "居酒屋,ディナー,DJ,ダンス,ライブ");
        insert("猫に癒されたい", "猫カフェ,癒し,体験,室内スポット,宿泊");
        insert("大阪の定番を満喫したい", "定番,観光,大阪,千日前,道頓堀");
        insert("ひとりでじっくり楽しみたい", "老舗,地下,親密,アコースティック,小規模");
        insert("スパイシーなものが食べたい", "カレー,ネパール料理,インド料理,ナン,本格");
    }

}
