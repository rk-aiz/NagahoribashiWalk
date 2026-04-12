package com.example.nagahoribashi_walk.type;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.IntUnaryOperator;

import lombok.Getter;

@Getter
public enum FortuneRank {

    DAIKICHI("大吉", 10000, "rank-daikichi", 10, List.of(
            "今日のあなたは無敵。たこも驚いている。",
            "運気が爆発中。何をやっても大丈夫な一日。",
            "チャンスの波が来ている。乗り遅れるな！",
            "思い切って動け。後悔は明日の自分に任せればいい。",
            "すべての星があなたに味方している。珍しいことだ。",
            "大吉とはつまり、今日は何でも許される日である。",
            "運を使い果たす勢いで楽しんでいい。",
            "今日のあなたは選ばれし者。たこやき神社も認めた。",
            "やりたいことを全部やれ。今日だけは宇宙があなたの味方。",
            "今日できないことは一つもない。多分。")),
    CHUKICHI("中吉", 5000, "rank-chukichi", 25, List.of(
            "悪くない一日。むしろかなりいい。",
            "急がなくていい。ちゃんと前に進んでいる。",
            "平均以上の運。これで文句を言ったらバチが当たる。",
            "今日は小さな幸せを3つ見つけてみよう。",
            "中吉は大吉より珍しい説がある。今日は運がいい方。",
            "無理に攻めなくていい。じっくりいこう。",
            "中吉を引いた者は謙虚であれ。それが吉に転じる道。",
            "今日の運気は安定株。手堅く行動するが吉。",
            "周りを見渡してみると、意外と良いことだらけかもしれない。",
            "堅実な運気。地味だが確実に良いことが起きる。")),
    SHOKICHI("小吉", 2000, "rank-shokichi", 30, List.of(
            "悪くはない。そう、悪くはない。",
            "小さな幸運がそっとそばにある。見逃すな。",
            "大きなことより、今日は小さなことを丁寧にやろう。",
            "じわじわ運気上昇中。焦らなくていい。",
            "幸運はそこらへんをウロウロしている。見つけてあげよう。",
            "欲張らなければ十分良い一日になる。",
            "小吉の「小」は控えめな輝きの意味だと思えばいい。",
            "今日できることを今日やろう。それだけで吉。",
            "小吉だからといって油断は禁物。着実にいけ。",
            "今日は「小さいけど確かな一日」にしてみよう。")),
    SUEKICHI("末吉", 1000, "rank-suekichi", 25, List.of(
            "今は仕込みの時。いつか花開く。",
            "焦らない。まだ運が温まっていない。",
            "末吉とはつまり「これから良くなる」という意味である。",
            "今日は何もしないのが最善かもしれない。",
            "じっくり待て。待った分だけ良いことが来る…はず。",
            "今日は守りの日。攻めるなら明日にしよう。",
            "末吉を引いた者は忍耐力がある。それ自体が才能だ。",
            "運の充電中。今日は節約モードで過ごそう。",
            "下がり切ったら上がるだけ。今がそのタイミングかも。",
            "運気は末端から上昇中。もう少しで頂点へ。")),
    KYO("凶", 50, "rank-kyo", 10, List.of(
            "大丈夫。明日は別の日だ。",
            "凶を引いた勇者よ、今日は大人しくたこやきを食べよう。",
            "凶は実は縁起が良いという説がある。ウソかもしれないが。",
            "今日は宇宙があなたに休息を命じている。",
            "何もしなければ何も起きない。今日は何もしない作戦で。",
            "凶を引けるのもある意味才能。ポジティブに。",
            "たこやき神社のたこは凶を引いた人に特に優しい。",
            "今日は厄を落とせた日だと思おう。明日から運気上昇の予感。",
            "凶を引いたあなたへ。それでも長堀橋さんぽは楽しい。",
            "運が悪いのか、使い果たしたのか。どちらにせよ今日は休め。"));

    private final String displayName; // 表示名
    private final int point; // 付与ポイント
    private final String cssClass; // テンプレートのCSSクラス
    private final int weight; // 抽選の重み（合計100）
    private final List<String> messages; // 一言メッセージ候補

    FortuneRank(String displayName, int point, String cssClass, int weight, List<String> messages) {
        this.displayName = displayName;
        this.point = point;
        this.cssClass = cssClass;
        this.weight = weight;
        this.messages = messages;
    }

    /** ランクの一言メッセージをシードから決定論的に選ぶ */
    public String pickMessage(long seed) {
        return messages.get(new Random(seed).nextInt(messages.size()));
    }

    // IntUnaryOperator: int を受け取って int を返す関数型インターフェース
    public static FortuneRank draw(IntUnaryOperator randomSource) {
        int total = Arrays.stream(values()).mapToInt(r -> r.weight).sum();
        int rand = randomSource.applyAsInt(total); // total を渡し、0〜total-1 の値を期待
        int cumulative = 0;
        for (FortuneRank rank : values()) {
            cumulative += rank.weight;
            if (rand < cumulative)
                return rank;
        }
        return KYO;
    }

}
