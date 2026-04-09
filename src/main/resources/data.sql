
-- ============================================================
-- data.sql  長堀橋さんぽ 初期データ（開発用）
-- ============================================================
-- schema.sql 実行後に流す。実行順序は依存関係に従うこと:
--   users → categories → sub_categories → spots → favorites → reviews
-- ============================================================


-- ユーザー
-- パスワードはすべて同じ文字列を BCrypt でハッシュしたもの
INSERT INTO users (username, password, email, role, display_name, point) VALUES

-- パス : Admin@2026
('admin', '$2a$10$lERDBGAj7aGIPChXyUEj.OIZHO50N/9BnVn6EO6T5uOauUVkH37Ry', 'admin@nagahori.com', 'ADMIN', '管理者', 0),

-- パス : User@2026
('user1', '$2a$10$ah6tn/KV6rc34sdUGh0wF.5UEZbs0UG5ObXiW0c72MfiEhAW3ljFa', 'user1@nagahori.com', 'USER', 'ユーザー1', 1000),
('user2', '$2a$10$1V6vPNeCXWU/3eaF8f9yC.LFMXMOELE2badil7dOyXreMpjqHKdXm', 'user2@nagahori.com', 'USER', 'ユーザー2', 2000),
('user3', '$2a$10$1V6vPNeCXWU/3eaF8f9yC.LFMXMOELE2badil7dOyXreMpjqHKdXm', 'user3@nagahori.com', 'USER', 'ユーザー3', 3000),
('user4', '$2a$10$1V6vPNeCXWU/3eaF8f9yC.LFMXMOELE2badil7dOyXreMpjqHKdXm', 'user4@nagahori.com', 'USER', 'ユーザー4', 4000),
('user5', '$2a$10$1V6vPNeCXWU/3eaF8f9yC.LFMXMOELE2badil7dOyXreMpjqHKdXm', 'user5@nagahori.com', 'USER', 'ユーザー5', 5000),

-- パス : Demo@2026
('demouser', '$2a$10$CKcgVyyKiMiVPZS88S.EeON2w6FKEVr.1wVQVVa8d6vDi74mGKEmq', 'demouser@example.com', 'USER', 'デモユーザー', 10000);

-- カテゴリ
-- is_default=TRUE の「その他」はフォールバック先。display_order=NULL で常に末尾に表示。
-- categories に INSERT するたびに add_default_sub_category トリガーが発火し、
-- 「未分類」サブカテゴリ（is_default=TRUE, display_order=NULL）を自動生成する。
INSERT INTO categories (name, display_order, is_default) VALUES
('その他', NULL, true),    -- フォールバック用デフォルトカテゴリ（削除不可）
('グルメ', 1, false),
('観光スポット', 2, false),
('ショッピング', 3, false),
('娯楽', 4, false),
('カフェ', 5, false);

-- サブカテゴリ
-- display_order はカテゴリ内での表示順（1始まりの連番）
-- is_default=TRUE の「未分類」はカテゴリINSERT時のトリガーが自動挿入するため、ここでは書かない
-- category_id の対応: 1=その他, 2=グルメ, 3=観光スポット, 4=ショッピング, 5=娯楽, 6=カフェ
INSERT INTO sub_categories (category_id, name, display_order) VALUES
(2, '居酒屋',             1),  -- グルメ
(2, '外国料理屋',         2),  -- グルメ
(2, 'カレー',             3),  -- グルメ
(2, 'ラーメン',           4),  -- グルメ
(2, 'たこ焼き',           5),  -- グルメ
(2, 'イタリアン',         6),  -- グルメ
(2, 'ハンバーガー',       7),  -- グルメ
(2, '韓国料理',           8),  -- グルメ
(2, '中華料理',           9),  -- グルメ
(2, '焼肉',              10),  -- グルメ
(3, '神社',               1),  -- 観光スポット
(3, '公園',               2),  -- 観光スポット
(4, 'ガチャ',             1),  -- ショッピング
(4, 'ドラッグストア',     2),  -- ショッピング
(4, 'スーパーマーケット', 3),  -- ショッピング
(4, '雑貨屋',             4),  -- ショッピング
(4, '100円ショップ',      5),  -- ショッピング
(5, 'ライブハウス',       1),  -- 娯楽
(5, '劇場',               2),  -- 娯楽
(5, 'サウナ',             3),  -- 娯楽
(5, '猫カフェ',           4),  -- 娯楽
(6, 'カフェ',             1);  -- カフェ

-- スポット
-- sub_category_id はサブクエリで名前引きしている。
-- 同名のサブカテゴリが複数カテゴリに存在する場合は意図しないIDが入る可能性があるため注意。
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('なんばグランド花月', (SELECT id FROM sub_categories WHERE name = '劇場'), '542-0075大阪府大阪市中央区難波千日前11-6', '一般的 10:00～22:00', '不定休(HP詳細)', '3000円～', '新喜劇と漫才が楽しめる笑いの殿堂', 'https://ngk.yoshimoto.co.jp/', 'https://maps.app.goo.gl/Dcc4azPiUHdewJXf7', 'お笑い,吉本,新喜劇,劇場,観光,定番');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('よしもと漫才劇場', (SELECT id FROM sub_categories WHERE name = '劇場'), '542-0075大阪府大阪市中央区難波千日前12-7 YES-NAMBAビル5F', '一般的 10:00～22:00', '不定休(HP詳細)', '1300円～', '次世代のお笑いスターを発掘できる', 'https://manzaigekijyo.yoshimoto.co.jp/', 'https://maps.app.goo.gl/x9TGRYoN5K2dA8uh7', 'コント,漫才,若手芸人,お笑い,劇場');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('よしもと道頓堀シアター', (SELECT id FROM sub_categories WHERE name = '劇場'), '542-0071大阪府大阪市中央区道頓堀1-7-21 中座くいだおれビル 6F', '一般的 10:00～22:00', '不定休(HP詳細)', '2000円～', '食べて笑って楽しめる英語対応お笑い劇場', 'https://dotonbori.yoshimoto.co.jp/', 'https://maps.app.goo.gl/AQ4FLfSmRjwYMVNu9', '道頓堀,お笑い,シアター,観光');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('牛かつ　富田', (SELECT id FROM sub_categories WHERE name = '居酒屋'), '〒226-0011大阪府大阪市浪速区難波中2-3-1 2F', '11:00～23:00', '不定休', '1500円～', '自分好みに焼いて楽しむ、体験型の牛かつ専門店', 'null', 'https://maps.app.goo.gl/LD46sFzsooMaKVfr8', '牛かつ,人気,行列,グルメ');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('たこ焼き道楽わなか千日前本店', (SELECT id FROM sub_categories WHERE name = 'たこ焼き'), '542-0075大阪府大阪市中央区難波千日前11-19', '(月～金) 10:30～21:00(土・日・祝) 9:30～21:00', '定休日：なし（年中無休）
※営業時間や定休日は予告なく変更される可能性がありますので、訪問前に店舗へ直接確認することをおすすめします。', '500円～', '外はカリッ、中はトロッ！大阪名物たこ焼きの老舗', 'http://takoyaki-wanaka.com/#content04', 'https://maps.app.goo.gl/mwr9y4mG1geNhwQp6', '大阪,たこ焼き,有名,食べ歩き,千日前');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('中華そばふじい難波千日前店', (SELECT id FROM sub_categories WHERE name = 'ラーメン'), '542-0076大阪府大阪市中央区難波1-3-14', '11:00～23:30', '火曜日', '昼 900円～/夜 1000円～', '昔懐かしい醤油ベースの中華そばに背脂のコクが効いた、大阪名物ラーメン', 'https://ra-men.co/', 'https://maps.app.goo.gl/32RjwwNXdrs1gfrV7', '中華そば,ラーメン,醤油ラーメン,餃子,あっさり,人気店');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('ねこ浴場＆ねこ旅籠', (SELECT id FROM sub_categories WHERE name = '猫カフェ'), '〒542-0082 大阪府大阪市中央区島之内1丁目14-29', '12:00～20:00', 'なし（猫の健康管理のため月一回不定休あり）', 'カフェ利用 ¥220〜¥4,180／宿泊 ¥16,500〜', '遊んで、くつろいで、泊まれる猫カフェ体験', 'https://www.neco-republic.jp/necoyokujo/', 'https://maps.app.goo.gl/HC86os5QrHeubyGu8', '猫カフェ,癒し,体験,室内スポット,宿泊');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('本宮的茶 大阪　タピオカミルクティー専門店｜BEN GONG’S TEA Osaka', (SELECT id FROM sub_categories WHERE name = 'カフェ'), '〒542-0082大阪府大阪市中央区島之内１丁目２１−３０ １階', '11:00～23:00 (LO.22:30)', 'なし', '700円〜', 'もちもちタピオカと本格中国茶の専門店', 'https://www.bengongstea-osaka.app/', 'https://maps.app.goo.gl/VUfp514nvKnL8khD7', 'タピオカ,人気,中国茶,インスタ映え');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('ハーリス　なんばマルイ店', (SELECT id FROM sub_categories WHERE name = 'カフェ'), '〒542-0076 大阪府大阪市中央区難波３丁目８−９ なんばマルイ １階', '8：30～22：00', '不定休（なんばマルイに準ずる）', '500円～', '韓国発のおしゃれ空間で楽しむ本格カフェ体験', 'https://www.0101.co.jp/085/shop-guide/shop-detail.html?shop_id=20898', 'https://maps.app.goo.gl/SvLMEqUKESYffKkr7', 'カフェ,韓国カフェ,おしゃれ,なんばマルイ,スイーツ');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('豚と炭火 こぶた家 なんばパークス店', (SELECT id FROM sub_categories WHERE name = '居酒屋'), '〒556-0011 大阪府大阪市浪速区難波中２丁目１０−７０ なんばパークス 6F', '11:00～23:00 (LO 22:00)', 'なし（年中無休）', '1000円～', '炭火焼きとせいろ蒸しで楽しむ、イベリコ豚と野菜の専門店', 'https://nambaparks.com/shopresearch/677', 'https://maps.app.goo.gl/fjSTLx1aVdF6PMEn9', 'なんばパークス,豚料理,炭火,ディナー');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('坐摩神社', (SELECT id FROM sub_categories WHERE name = '神社'), '〒541-0056 大阪府大阪市中央区久太郎町４丁目渡辺３ 渡辺3号', '【開門時間】
平日　７：３０～１７：３０
土日祝日　７：３０～１７：００', '年中無休（詳細はHP参照）', 'ご自身の塩梅', '都会の中で静かにご利益を感じられるパワースポット！', 'http://www.ikasuri.or.jp/', 'https://maps.app.goo.gl/Ws8CGgGEEkNw6cfy8', '大阪,神社,パワースポット,縁結び,都会の中,静か');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('サムハラ神社', (SELECT id FROM sub_categories WHERE name = '神社'), '〒550-0012 大阪府大阪市西区立売堀２丁目５−２６', '参拝はいつでも自由にできます（HP記載あり）', '年中無休（詳細はHP参照）', 'ご自身の塩梅', '強力な厄除けで有名な知る人ぞ知る神社', 'https://samuhara.or.jp/', 'https://maps.app.goo.gl/DFkW2mED1zVWLERy8', '神社,厄除け,最強,ご利益,指輪,お守り');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('難波八坂神社', (SELECT id FROM sub_categories WHERE name = '神社'), '〒556-0016 大阪府大阪市浪速区元町２丁目９−１９', '開門：6:00〜17:00
お守り・授与所：9:00〜17:00', '年中無休（詳細はHP参照）', 'ご自身の塩梅', 'インパクト抜群の獅子殿で写真映えもご利益も◎', 'https://nambayasaka.jp/', 'https://maps.app.goo.gl/W69DuafJpQK6vbHd6', '難波,神社,獅子殿,写真映え,パワースポット,観光');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('＃C-pla 大阪心斎橋筋北店', (SELECT id FROM sub_categories WHERE name = 'ガチャ'), '〒542-0081 大阪府大阪市中央区南船場３丁目１０−１１ リンクス心斎橋 1F', '10:00～23:00', '記載なし（HP要参照）', '200円～', '種類豊富でつい回したくなるガチャ天国', 'https://toshin.jpn.com/shop/%e5%a4%a7%e9%98%aa%e5%bf%83%e6%96%8e%e6%a9%8b%e7%ad%8b%e5%8c%97%e5%ba%97/', 'https://maps.app.goo.gl/SVrZncA2x6K4FyC77', 'ガチャガチャ,専門店,カプセルトイ,人気');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('＃C-pla大阪心斎橋筋1丁目店', (SELECT id FROM sub_categories WHERE name = 'ガチャ'), '〒542-0085 大阪府大阪市中央区心斎橋筋１丁目５−２１', '10:00～23:00', '記載なし（HP要参照）', '200円～', '最新ガチャが揃うトレンドスポット！設置ボックス数は1297種類！(周辺では最大数)', 'https://toshin.jpn.com/shop/%e5%a4%a7%e9%98%aa%e5%bf%83%e6%96%8e%e6%a9%8b%e7%ad%8b1%e4%b8%81%e7%9b%ae%e5%ba%97/', 'https://maps.app.goo.gl/xzFoCWDBNfxxUmSQA', 'ガチャガチャ,大型店,カプセルトイ,最新');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('＃C-pla+ 大阪心斎橋筋店', (SELECT id FROM sub_categories WHERE name = 'ガチャ'), '〒542-0085 大阪府大阪市中央区心斎橋筋２丁目７−３', '10:00~23:00', '記載なし（HP要参照）', '200円～', 'レア系や大人向けガチャも楽しめる', 'https://toshin.jpn.com/shop/%e5%a4%a7%e9%98%aa%e5%bf%83%e6%96%8e%e6%a9%8b%e7%ad%8b%e5%ba%97-3/', 'https://maps.app.goo.gl/346FG7KYLecj2D3k6', 'ガチャガチャ,レア,大人向け,カプセルトイ');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('#C-pla 大阪心斎橋筋南店1号館', (SELECT id FROM sub_categories WHERE name = 'ガチャ'), '〒542-0085 大阪府大阪市中央区心斎橋筋２丁目６−3 1-2階', '10:00～23:00', '記載なし（HP要参照）', '200円～', '店内広々でゆっくり選べるガチャ専門店', 'https://toshin.jpn.com/shop/%e5%a4%a7%e9%98%aa%e5%bf%83%e6%96%8e%e6%a9%8b%e7%ad%8b%e5%8d%97%e5%ba%971%e5%8f%b7%e9%a4%a8/', 'https://maps.app.goo.gl/RhSTs6hQhU9UR6RK6', 'ガチャガチャ,専門店,人気');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('#C-pla 大阪心斎橋筋南店2号館', (SELECT id FROM sub_categories WHERE name = 'ガチャ'), '〒542-0085 大阪府大阪市中央区心斎橋筋２丁目３−２７ 心央ビル 1階', '10:00~22:00', '記載なし（HP要参照）', '200円～', '1号館と合わせて巡ると満足度アップ', 'https://toshin.jpn.com/shop/%E5%A4%A7%E9%98%AA%E5%BF%83%E6%96%8E%E6%A9%8B%E7%AD%8B%E5%8D%97%E5%BA%972%E5%8F%B7%E9%A4%A8/', 'https://maps.app.goo.gl/Rcc1p5pStDFbWAHP7', 'ガチャガチャ,店舗巡り,カプセルトイ,観光');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('想 ‐SOU- SAUNA', (SELECT id FROM sub_categories WHERE name = 'サウナ'), '〒542-0082 大阪府大阪市中央区島之内１丁目５−１１', '9:00 ～ 23:00（最終受付 21:30）', '不定休', '4,500円～', 'しゃれ空間で整える大人のリラックスサウ', 'https://sou-sauna.jp/', 'https://maps.app.goo.gl/sYLuFS7fC5frADHg9', 'サウナ,おしゃれ,個室サウナ,整う,リラックス');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('炭火焼き鳥 鶏尽', (SELECT id FROM sub_categories WHERE name = '居酒屋'), '〒542-0075 大阪府大阪市中央区難波千日前２−２１', '(平日) 17:00〜25:30L.O
(土・日・祝日)12:00〜25:30L.O', '年中無休', '3,000円～', '炭火の香りがたまらない本格焼き鳥で締めに最高', 'https://torijin.jp/', 'https://maps.app.goo.gl/3vKoStf9LaJ2BvSy7', '心斎橋,焼き鳥,炭火,居酒屋,ディナー,人気店');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('黒毛和牛タンとハラミ焼肉ごりちゃん心斎橋店', (SELECT id FROM sub_categories WHERE name = '焼肉'), '〒542-0085 大阪府大阪市中央区心斎橋筋１丁目３−１３ 茶茶心斎橋ビル 2階', '11:00～15:00
17:00～07:00', '記載なし（HP要参照）', '5,000円～', '食べログHOTレストラン2年連続受賞、A5和牛と名物タンが楽しめる実力派焼肉店！', NULL, 'https://maps.app.goo.gl/tSpafoFfN7YLkZix9', '心斎橋,焼肉,A5黒毛和牛,タン,ハラミ,人気店,食べログホットレストラン');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('なんばHatch', (SELECT id FROM sub_categories WHERE name = 'ライブハウス'), '大阪市浪速区湊町1-3-1', '公演による (通常17:00-)', '不定休', 'チケット代（別途ドリンク代）', '湊町リバープレイス内。音響・照明が最高峰の大規模ホール。', 'http://www.namba-hatch.com/', 'https://maps.app.goo.gl/ShT4XCMtmwiyr3Ed8', 'ライブハウス,音楽,コンサート,中規模,スタンディング');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('Zepp Namba', (SELECT id FROM sub_categories WHERE name = 'ライブハウス'), '大阪市浪速区敷津東2-1-39', '公演による (通常18:00-)', '不定休', 'チケット代（別途ドリンク代）', '国内最大級。圧倒的な没入感と迫力を楽しめる。', 'https://www.zepp.co.jp/hall/namba/', 'https://maps.app.goo.gl/k5vLJhmsYUQQJFvb7', 'ライブハウス,大型,コンサート,アーティスト,Zepp');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('梅田芸術劇場', (SELECT id FROM sub_categories WHERE name = '劇場'), '大阪市北区茶屋町19-1', '公演による', '不定休', 'チケット代', '宝塚から最新海外ミュージカルまで、最高の臨場感で味わえる『関西エンタメの聖地』', 'https://www.umegei.com/', 'https://maps.app.goo.gl/yTcvkKRjzc7NL6hp6', '劇場,ミュージカル,舞台,演劇,観劇');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('心斎橋BIGCAT', (SELECT id FROM sub_categories WHERE name = 'ライブハウス'), '大阪市中央区西心斎橋1-6-14', '公演による', '不定休', 'チケット代（別途ドリンク代）', 'アメリカ村「BIG STEP」内。プロも認める定番のハコ。', 'http://bigcat-live.com/', 'https://maps.app.goo.gl/E6NaYowt1hVTTZfN6', 'ライブハウス,音楽,インディーズ,バンド');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('Music Club JANUS', (SELECT id FROM sub_categories WHERE name = 'ライブハウス'), '大阪市中央区東心斎橋2-4-30', '12:00〜23:00 (公演による)', '無休', 'チケット代（別途ドリンク代）', 'お洒落なバーカウンター併設。大人なライブにも最適。', 'http://www.arm-live.com/janus/', 'https://maps.app.goo.gl/z9jF59PGkTnSwHVj8', 'ライブハウス,音楽,バンド,ライブ,地下');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('OSAKA MUSE', (SELECT id FROM sub_categories WHERE name = 'ライブハウス'), '大阪市中央区心斎橋筋1-5-6', '18:00〜21:30 (公演による)', '年中無休', 'チケット代（別途ドリンク代）', '1987年創業の老舗。バンドマンの登竜門的存在。', 'http://www.osaka-muse.com/', 'https://maps.app.goo.gl/D1KKwrktvWXwnDV5A', 'ライブハウス,音楽,バンド,ライブ,老舗');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('なんばMele', (SELECT id FROM sub_categories WHERE name = 'ライブハウス'), '大阪市浪速区元町1-2-2', '17:00〜22:00 (公演による)', '不定休', 'チケット代（別途ドリンク代）', 'ロック・ガレージ系に強い。アングラで熱い夜。', 'https://namba-mele.com/', 'https://maps.app.goo.gl/R38UVBG1KHEYhBzMA', 'ライブハウス,音楽,小規模,アコースティック,親密');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('心斎橋SUNHALL', (SELECT id FROM sub_categories WHERE name = 'ライブハウス'), '大阪市中央区西心斎橋2-9-28', '公演による', '不定休', 'チケット代（別途ドリンク代）', 'アメ村のど真ん中。アイドルからヒップホップまで多ジャンル。', 'https://sunhall.jp/', 'https://maps.app.goo.gl/A9J9QBaLVv2x3y4HA', 'ライブハウス,音楽,地下,クラブ,DJ,ダンス');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('KoKuMiN クリスタ長堀店', (SELECT id FROM sub_categories WHERE name = 'ドラッグストア'), '中央区南船場2丁目 長堀地下街2号', '8:00 - 22:00', '2月第3月曜日・年末年始', 'null', '駅直結で雨に濡れない： 堺筋線や長堀鶴見緑地線の改札からすぐなので、移動のついでに寄るのに最も便利です。', 'https://store.welcia.co.jp/welcia/spot/detail?code=7690D', 'https://maps.app.goo.gl/TwLKvicZhqvTN4HC8', 'ドラッグストア,コスメ,日用品,お土産,地下街');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('スギ薬局 南船場店', (SELECT id FROM sub_categories WHERE name = 'ドラッグストア'), '大阪市中央区南船場二丁目1番地3号　PHOENIX南船場1階', '08:00〜23:30', '無休', 'null', '夜遅くまで営業', 'https://www.sugi-net.jp/stores/001569', 'https://maps.app.goo.gl/N4fy2PbgLqJ2wxK19', 'ドラッグストア,薬局,日用品,医薬品,便利');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('玉出', (SELECT id FROM sub_categories WHERE name = 'スーパーマーケット'), '〒542-0082 大阪府大阪市中央区島之内１丁目１２−１０', '24 時間営業', '無', NULL, '24 時間営業', 'https://supertamade.co.jp/', 'https://maps.app.goo.gl/P93EgbUk3XYHYhpP6', 'スーパー,激安,大阪名物,ローカル,24時間');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('SARISARI MAMA（PHILIPPINE FOODS）', (SELECT id FROM sub_categories WHERE name = '雑貨屋'), '島之内２丁目６−２０ ブロンズハイツ 202号06-6484-7627', '12:00～22:00', '無', NULL, 'フィリピン雑貨', 'https://shop.philippinefoods-sarisarimama.com/', 'https://maps.app.goo.gl/MzcZdpKYTziTNPU58', 'フィリピン料理,外国料理,エスニック,本格,異国グルメ');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('タワンタイ', (SELECT id FROM sub_categories WHERE name = '外国料理屋'), '大阪府大阪市中央区南船場２-６-２１ グラン・ビルド心斎橋 １F', 'ランチ 11:30 〜 15:00
ディナー 17:00 〜 23:00', '無', '～4000', 'タイ料理', 'https://k118500.gorp.jp/', 'https://maps.app.goo.gl/SdLmrwxfn6y2BFrr5', '難波,タイ料理,外国料理,エスニック,本格,パッタイ,トムヤムクン');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('ヒマラヤン', (SELECT id FROM sub_categories WHERE name = 'カレー'), '大阪府大阪市中央区南船場２-１０-１７南愛ビル２F', 'ランチ 11:00 〜 15:00 L.O. 14:30
ディナー 17:00 〜 22:00 L.O. 21:30', '無', '～3000', 'インド料理 インドカレー ネパール料理 カレー', 'https://tabelog.com/osaka/A2701/A270201/27062700/', 'https://maps.app.goo.gl/N9392C4znNa7z8hEA', 'ネパール料理,インド料理,カレー,ナン,エスニック');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('オーサカバインミー', (SELECT id FROM sub_categories WHERE name = '外国料理屋'), '大阪府大阪市中央区南船場１-１３-１５ 長堀三栄プラザ１０６', '[月〜金]
10:00 〜 20:00
[土・祝]
10:00 〜 17:00
[日]
10:00 〜 15:00', '無', '～1000', 'ベトナム料理 スイーツ', 'https://x.com/osaka_banhmi', 'https://maps.app.goo.gl/kr3QiFkjL5nqcA8fA', '心斎橋,ベトナム料理,バインミー,サンドイッチ,食べ歩き,テイクアウト');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('韓国料理 HANSSAM ハンサム 難波心斎橋店', (SELECT id FROM sub_categories WHERE name = '韓国料理'), '大阪府大阪市中央区島之内１-２１-１９ B１F', '[全日]
11:00 〜 22:30 L.O. 21:00', '無', NULL, '韓国料理', 'https://tabelog.com/osaka/A2701/A270201/27151669/', 'https://maps.app.goo.gl/1LgGypkA3yhLUcuQ8', '韓国料理,焼肉,サムギョプサル,外国料理,本格');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('トルコ料理ナザール心斎橋本店', (SELECT id FROM sub_categories WHERE name = '外国料理屋'), '大阪府大阪市中央区東心斎橋１-１６-１３ マツムラビル２F', '[全日]
ディナー 17:30 〜 23:00
[日・土・祝]
ランチ 12:00 〜 15:00', '無', '～4000', 'トルコ料理 居酒屋 テイクアウト', 'http://www.nazar.jp/', 'https://maps.app.goo.gl/YCZz2Xj9G3vcSJ8x9', '心斎橋,トルコ料理,ケバブ,外国料理,エスニック,本格');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('EL PANCHO', (SELECT id FROM sub_categories WHERE name = '外国料理屋'), '大阪府大阪市中央区心斎橋筋１-１０-１ 心斎橋タワービル ８F', '[全日]
11:30 〜 23:30 L.O. 22:30', '無', '～4000', 'メキシコ料理 ダイニングバー ハンバーガー タコス', 'https://www.instagram.com/elpancho_osaka/', 'https://maps.app.goo.gl/kpYZne4t2DG7DKR56', 'メキシコ料理,タコス,外国料理,ラテン,陽気');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('老四川紅燒牛肉麺', (SELECT id FROM sub_categories WHERE name = 'ラーメン'), '〒542-0082 大阪府大阪市中央区𡷊之内, ２丁目８−１８', '11:00～20:30', '日曜', '～1000', '中華・ラーメン', 'https://tabelog.com/osaka/A2701/A270202/27146840/', 'https://maps.app.goo.gl/QEtkJByJVRN2aEfy9', '中華料理,牛肉麺,四川,本格,辛い,麺');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('Naga～n cucina italiana （ナガーン クッチーナ イタリアーナ）', (SELECT id FROM sub_categories WHERE name = 'イタリアン'), '〒542-0083 大阪府大阪市中央区東心斎橋１丁目３−７ 1F', '17:30～22:00', '日曜', '1,000～2,000', 'イタリア料理店', 'http://www.naga-n.jp/', 'https://maps.app.goo.gl/e3inDrAyHs2HLeJU8', 'イタリアン,パスタ,ピザ,おしゃれ,ディナー');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('橋本屋', (SELECT id FROM sub_categories WHERE name = 'カレー'), '〒542-0081 大阪府大阪市中央区南船場２丁目２−21ｰ101', '11時45分～13時00分', '金曜日、土曜日、日曜日、祝日', '￥1,000～2,000', '平日のお昼のみという限られた時間で営業している人気のスパイスカレー店です。', NULL, 'https://maps.app.goo.gl/Erqm3F36MAqHd3qP8', 'スパイスカレー,ランチ,人気店, 行列, 激レア');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('定食堂 金剛石', (SELECT id FROM sub_categories WHERE name = 'カレー'), '〒542-0066 大阪府大阪市中央区瓦屋町１丁目８−２５', '11時30分～14時00分、18時00分～21時00分
(火曜日: 11時30分～15時30分)', '水曜日', '￥1,000～2,000', 'スパイスカレーやエスニックな定食メニューが連日大人気のお店です。', 'https://www.twitter.com/currykenmiconos', 'https://maps.app.goo.gl/41Abtq7Yce3hc2FP7', 'バスマティライス,魯肉飯,定食屋,豆乳,ポリヤル,ミシュラン,マライ');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('CRITTERS BURGER / クリッターズバーガー', (SELECT id FROM sub_categories WHERE name = 'ハンバーガー'), '〒542-0086 大阪府大阪市中央区西心斎橋１丁目１０−３５ １F', '11時00分～22時00分', '無し', '￥1,000～2,000', 'アメリカン スタイルのジューシーなハンバーガーとフライドポテト、サラダ、ビールを楽しめる気さくな飲食店。', 'http://critters.jp/', 'https://maps.app.goo.gl/Y7oB399H1DLkT3wM9', 'ハンバーガー,グルメ,ランチ,アメリカン,人気店');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('日本橋 さか一', (SELECT id FROM sub_categories WHERE name = 'ラーメン'), '〒542-0082 大阪府大阪市中央区島之内２丁目１１−３', '日曜日
7時00分～13時30分
平日
9時30分～14時00分', '土曜日、木曜日', '￥1,000～2,000', 'ストレート平打ち中太麺。
煮干し感強めの醤油スープが美味しいです。', 'https://twitter.com/nipponbashisak1', 'https://maps.app.goo.gl/gWL2HKYLLyms4GTS8', 'ランチ,ディナー,人気,麺スタグラム,醤油ラーメン');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('たこりき', (SELECT id FROM sub_categories WHERE name = 'たこ焼き'), '〒542-0066 大阪府大阪市中央区瓦屋町１丁目６−１', '12時00分～16時00分', '月曜日、火曜日', '￥1,000～2,000', 'オリジナルたこ焼きがさまざまな味付けで味わえる。グリル料理やワインなども提供している。カウンター席のみ。テイクアウト可。', 'http://www.takoriki.jp/', 'https://maps.app.goo.gl/LEKnPrEBcKnx2HMF6', 'たこ焼き,食べ歩き,大阪名物,谷町');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('カフェ＆カレー ボタ', (SELECT id FROM sub_categories WHERE name = 'カフェ'), '〒542-0083 大阪府大阪市中央区東心斎橋１丁目８−２０', '12:00 - 21:00', '水曜日', '￥1,000～2,000', '長屋を改装した店内は、レトロで趣きのある雰囲気。看板メニューのカレーはテイクアウトも可能。チャイやラッシー、スイーツも楽しめる。', 'https://www.buttah.net/', 'https://maps.app.goo.gl/1SqaGS5pFWRLrKnS9', 'ランチ,ディナー,アルコール,コーヒー,ビール,ワイン,カジュアル');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('中国料理 艶家', (SELECT id FROM sub_categories WHERE name = '中華料理'), '〒542-0082 大阪府大阪市中央区島之内１丁目２２−１２ ロイヤルハイツ寿 B1F', '[全日]
ランチ:11:30〜14:30 LO14:00
ディナー:17:00〜24:00 LO23:30', '無し', '￥1,000～4,000', '山東料理メインの町中華です。お一人でのご利用大歓迎です。味とコスパには自信があり、中国のお客様からもお褒め頂いております。', 'https://tsuyakenagahoribashi.foodre.jp/', 'https://maps.app.goo.gl/G4Pu7oohmH6JKs6Z8', '居酒屋,中華,ラーメン');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('イルピアット', (SELECT id FROM sub_categories WHERE name = 'イタリアン'), '〒542-0083 大阪府大阪市中央区東心斎橋１丁目６−３０', 'OPEN17:00～CLOSE 2:00
(L.O フード1:00/ドリンク1:30)', '火曜日', 'お手頃', '調理工程が見えるカウンター席で気軽に食事ができる隠れ家的な店。自家製麵を使用したパスタなど、多数のメニューを提供。', 'http://www.ilpiatto.info/honten/', 'https://maps.app.goo.gl/NnBkFtrcXhpm4FDZ7', 'イタリアン,パスタ,ピザ,肉料理,魚料理');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('御津公園（三角公園）', (SELECT id FROM sub_categories WHERE name = '公園'), '大阪府大阪市中央区西心斎橋2-11-34', '24時間', 'なし', '無料', 'アメリカ村の中心にあり、「三角公園」として親しまれている憩いの場。', NULL, 'https://maps.app.goo.gl/V4Y2dsZn6mzouAk56', '三角公園,アメリカ村,休憩,待ち合わせ');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('高津公園', (SELECT id FROM sub_categories WHERE name = '公園'), '大阪府大阪市中央区高津1丁目1', '24時間', 'なし', '無料', '高津宮に隣接する公園で、春には桜の名所としても知られる。', 'http://www.city.osaka.lg.jp/chuo/', 'https://maps.app.goo.gl/3TYWTQZCX9xCuDcF7', '高津宮,桜,散策,公園');
INSERT INTO spots (spot_name, sub_category_id, address, business_hours, closed_days, estimated_budget, details, website_url, gmap_url, keywords) VALUES ('道仁公園', (SELECT id FROM sub_categories WHERE name = '公園'), '〒542-0082 大阪府大阪市中央区島之内２丁目５', '24時間', 'なし', '無料', '都会の中の公園🛝', 'http://www.city.osaka.lg.jp/chuo/', 'https://maps.app.goo.gl/8onevUkU2zPtJokd7', 'ベンチ,自販機');




-- お気に入り
-- user_id: 1=admin, 2=user1, 3=user2, 4=user3, 5=user4, 6=user5, 7=demouser
-- spot_id はスポットの挿入順（INSERT順の連番）に対応する
INSERT INTO favorites (user_id, spot_id) VALUES

-- user1 (id=2): グルメ重視
(2,  5),   -- たこ焼き道楽わなか
(2,  6),   -- 中華そばふじい
(2, 20),   -- 炭火焼き鳥 鶏尽
(2, 35),   -- ヒマラヤン
(2, 44),   -- CRITTERS BURGER
(2, 45),   -- 日本橋 さか一
(2, 46),   -- たこりき

-- user2 (id=3): 観光・神社・癒し系
(3,  7),   -- ねこ浴場＆ねこ旅籠
(3, 12),   -- サムハラ神社
(3, 13),   -- 難波八坂神社
(3, 19),   -- 想 ‐SOU- SAUNA
(3, 43),   -- 定食堂 金剛石
(3, 47),   -- カフェ＆カレー ボタ
(3, 51),   -- 高津公園

-- user3 (id=4): エンタメ・ライブ好き
(4,  1),   -- なんばグランド花月
(4,  2),   -- よしもと漫才劇場
(4, 14),   -- C-pla 大阪心斎橋筋北店
(4, 15),   -- C-pla 大阪心斎橋筋1丁目店
(4, 22),   -- なんばHatch
(4, 23),   -- Zepp Namba
(4, 24),   -- 梅田芸術劇場
(4, 47),   -- カフェ＆カレー ボタ

-- user4 (id=5): カフェ・食べ歩き好き
(5,  8),   -- 本宮的茶
(5,  9),   -- ハーリス なんばマルイ店
(5, 34),   -- タワンタイ
(5, 37),   -- 韓国料理 HANSSAM
(5, 42),   -- 橋本屋
(5, 47),   -- カフェ＆カレー ボタ
(5, 50),   -- 御津公園（三角公園）

-- user5 (id=6): ライブ・夜遊び系
(6, 25),   -- 心斎橋BIGCAT
(6, 26),   -- Music Club JANUS
(6, 27),   -- OSAKA MUSE
(6, 28),   -- なんばMele
(6, 29),   -- 心斎橋SUNHALL
(6, 20),   -- 炭火焼き鳥 鶏尽
(6, 13);   -- 難波八坂神社

-- レビュー
-- 1ユーザーが同一スポットに投稿できるのは1件のみ（uq_reviews_user_spot 制約）
INSERT INTO reviews (user_id, spot_id, rating, comment) VALUES
-- なんばグランド花月 (spot_id=1)
(2, 1, 5, '新喜劇は何度観ても笑えます！大阪に来たら絶対に外せないスポット。'),
(3, 1, 4, '観光客向けですが、内容は本当に面白かった。チケットは事前購入がおすすめ。'),
(4, 1, 5, '初めて行きましたが最高でした！また来たい。'),

-- よしもと漫才劇場 (spot_id=2)
(2, 2, 4, '若手芸人の勢いがすごい。NGKより安くて楽しめます。'),
(5, 2, 3, '面白かったけど、出演者によって当たりはずれがある感じ。'),

-- よしもと道頓堀シアター (spot_id=3)
(3, 3, 4, '外国人の友達と行きました。英語対応があって助かりました。'),
(6, 3, 3, '観光感が強いですが、初めて大阪に来た人には良いと思う。'),

-- 牛かつ 富田 (spot_id=4)
(2, 4, 5, '自分で焼くスタイルが楽しい！レアで食べるのがおすすめです。'),
(4, 4, 4, '行列ができていましたが待った価値あり。ランチにまた来たい。'),
(5, 4, 5, '牛かつの概念が変わりました。最高においしい。'),

-- たこ焼き道楽わなか (spot_id=5)
(2, 5, 4, '外はカリッ中はトロ、まさに大阪のたこ焼き。観光客にもおすすめ。'),
(3, 5, 4, 'たくさん種類があって迷いましたが、定番のソースが一番好みでした。'),
(6, 5, 3, '美味しいけど少し割高な印象。でも味は間違いない。'),

-- 中華そばふじい (spot_id=6)
(2, 6, 5, '背脂の旨みがたまらない！大阪でラーメン食べるならここ一択。'),
(4, 6, 4, '懐かしい感じの醤油ラーメン。スープが最後まで飲める。'),

-- ねこ浴場＆ねこ旅籠 (spot_id=7)
(3, 7, 5, '猫に囲まれて至福のひとときでした。スタッフも親切で猫も人懐っこい。'),
(5, 7, 4, '泊まれる猫カフェというのが面白い。猫好きには天国です。'),
(6, 7, 5, '猫たちがとても元気で癒されました！また絶対来ます。'),

-- 本宮的茶 タピオカ (spot_id=8)
(2, 8, 4, 'タピオカがもちもちで美味しい。中国茶の種類も豊富。'),
(3, 8, 3, '甘さ控えめに調整できるのが嬉しい。でも少し高め。'),

-- ハーリス なんばマルイ店 (spot_id=9)
(4, 9, 4, '朝から開いていて助かりました。コーヒーのクオリティが高い。'),
(6, 9, 4, 'なんばマルイ内でアクセス抜群。インスタ映えする内装も良かった。'),

-- 豚と炭火 こぶた家 (spot_id=10)
(2, 10, 5, 'イベリコ豚のせいろ蒸しが絶品！野菜もたっぷり食べられます。'),
(5, 10, 4, 'なんばパークスで食べるなら絶対おすすめ。ランチお得です。'),

-- 坐摩神社 (spot_id=11)
(3, 11, 4, 'ビジネス街の中にひっそりある神社。都会の喧騒を忘れられる。'),
(4, 11, 3, '静かで落ち着ける場所。猫神様として有名らしい。'),

-- サムハラ神社 (spot_id=12)
(2, 12, 5, '神秘的な雰囲気に包まれた神社。指輪のお守りが人気で早めに行くべき。'),
(5, 12, 4, '厄除けのパワーを感じました。アクセスは少し分かりにくい。'),

-- 難波八坂神社 (spot_id=13)
(3, 13, 5, '獅子殿のインパクトが圧倒的！写真映えも抜群でした。'),
(6, 13, 4, 'ユニークな外観で一度は行く価値あり。観光スポットとして最高。'),

-- ガチャポン（C-pla 北店）(spot_id=14)
(2, 14, 4, 'ガチャの種類が豊富すぎてお金をかなり使ってしまいました笑'),
(4, 14, 4, '心斎橋でガチャを楽しむなら外せない。限定品も多い。'),

-- C-pla 1丁目店 (spot_id=15)
(3, 15, 5, '1297種類は圧巻！見ているだけでも楽しい。'),
(5, 15, 4, '最新のガチャがここに全部ある感じ。ついつい回しすぎてしまう。'),

-- C-pla+ 心斎橋筋店 (spot_id=16)
(2, 16, 3, '他の店舗と被ってる商品もあるが、限定品は狙い目。'),
(6, 16, 4, '大人向けのガチャが多くて好きです。'),

-- C-pla 南店1号館 (spot_id=17)
(3, 17, 4, '広い店内でゆっくり選べる。複数フロアあって見応えあり。'),

-- C-pla 南店2号館 (spot_id=18)
(4, 18, 3, '1号館と合わせて回るといい。こちらは少し小さめ。'),
(5, 18, 4, '1号館と2号館をはしごすると大満足できます。'),

-- 想 -SOU- SAUNA (spot_id=19)
(2, 19, 5, 'おしゃれな個室サウナで最高に整えました。また来たい！'),
(3, 19, 4, '値段は少し高めだが、プライベート空間でゆっくりできる。'),
(6, 19, 5, 'アロマの香りと照明が素晴らしい。完全に非日常でした。'),

-- 炭火焼き鳥 鶏尽 (spot_id=20)
(4, 20, 5, '炭火の香りがたまらない。焼き鳥のクオリティが本当に高い。'),
(5, 20, 4, '深夜遅くまでやっているので仕事終わりに最適。'),

-- 黒毛和牛焼肉ごりちゃん (spot_id=21)
(2, 21, 5, 'A5和牛が口の中でとろけました。食べログ受賞も納得のクオリティ。'),
(3, 21, 4, '少し高いですが、特別な日のディナーには最高の選択肢。'),

-- なんばHatch (spot_id=22)
(4, 22, 5, '音響が最高！ライブ会場としての完成度が高い。'),
(6, 22, 4, '大好きなアーティストのライブで訪れました。見やすいし音も最高。'),

-- Zepp Namba (spot_id=23)
(2, 23, 5, '国内最大級のハコ。迫力が段違い。立ち見でも楽しめました。'),
(5, 23, 4, 'アクセスが少し遠いが、ライブの没入感はここが一番。'),

-- 梅田芸術劇場 (spot_id=24)
(3, 24, 5, '宝塚の公演を観ました。舞台装置も衣装も豪華で感動しました。'),
(4, 24, 5, 'ミュージカルの臨場感が素晴らしい。関西のエンタメの中心地。'),

-- 心斎橋BIGCAT (spot_id=25)
(5, 25, 4, 'BIG STEP内でアクセス良好。音響も良くてライブを楽しめた。'),
(6, 25, 3, '定員が多いので前の方で見ないと遠く感じることも。'),

-- なんばMele (spot_id=28)
(2, 28, 4, 'ロック好きには堪らない雰囲気。小さいハコならではの一体感がある。'),
(4, 28, 3, 'アングラ感が強いが、熱いライブが観られる貴重な場所。'),

-- 玉出 (spot_id=32)
(3, 32, 5, '24時間営業で値段が破格！深夜に買い物できるのは本当に助かる。'),
(5, 32, 4, '品揃えが豊富で安い。大阪のスーパーといえばここ。'),

-- タワンタイ (spot_id=34)
(2, 34, 4, '本格的なタイ料理が食べられる。グリーンカレーが特においしかった。'),
(6, 34, 4, 'スパイスが効いていてクセになる味。コスパも良い。'),

-- ヒマラヤン (spot_id=35)
(3, 35, 5, 'ナンがふわふわで絶品。カレーの種類も多くて毎回悩む。'),
(4, 35, 4, '本場のインドカレーの味に感動。ランチセットがお得。'),

-- 韓国料理 HANSSAM (spot_id=37)
(2, 37, 4, '本格韓国料理が大阪でも食べられて嬉しい。チーズダッカルビが最高。'),
(5, 37, 4, '量が多くてコスパ抜群。友達と来るのにちょうどいい。'),

-- 橋本屋 (spot_id=42)
(3, 42, 5, '平日ランチのみという希少性。行列ができるのも納得の美味しさ。'),
(6, 42, 4, 'スパイスカレーとしてのクオリティが高い。早めに並ぶのが必須。'),

-- CRITTERS BURGER (spot_id=44)
(2, 44, 4, 'ジューシーなパティとフレッシュな野菜のバランスが完璧。'),
(4, 44, 4, 'アメリカンな雰囲気で食事を楽しめる。ポテトも美味しい。'),

-- カフェ＆カレー ボタ (spot_id=47)
(3, 47, 5, 'レトロな長屋の内装がおしゃれ。カレーもチャイも絶品です。'),
(5, 47, 4, '落ち着いた雰囲気でゆっくり食事できる。隠れ家感が好き。'),
(6, 47, 4, 'テイクアウトのカレーを公園で食べました。最高のランチ。'),

-- 御津公園（三角公園）(spot_id=50)
(2, 50, 3, 'アメ村の待ち合わせスポットとして定番。休憩できて助かる。'),
(4, 50, 3, '公園自体は小さいが、アメ村の中心にあるので便利。'),

-- 高津公園 (spot_id=51)
(3, 51, 4, '桜の季節に行きました。高津宮と合わせて散策するのがおすすめ。'),
(5, 51, 4, '都会の中でほっと一息できる公園。静かで落ち着きます。');


-- spot_photos (開発用初期データ)
INSERT INTO spot_photos (spot_id, photo_url, display_order) VALUES
-- 劇場
(1,  'images/theater_image_01.jpg', 1),   -- なんばグランド花月
(1,  'images/theater_image_01.jpg', 2),   -- なんばグランド花月
(1,  'images/theater_image_01.jpg', 3),   -- なんばグランド花月
(2,  'images/theater_image_02.jpg', 1),   -- よしもと漫才劇場
(3,  'images/theater_image_01.jpg', 1),   -- よしもと道頓堀シアター
(24, 'images/theater_image_02.jpg', 1),   -- 梅田芸術劇場
(24, 'images/theater_image_02.jpg', 2),   -- 梅田芸術劇場
(24, 'images/theater_image_02.jpg', 3),   -- 梅田芸術劇場
(24, 'images/theater_image_02.jpg', 4),   -- 梅田芸術劇場
(24, 'images/theater_image_02.jpg', 5),   -- 梅田芸術劇場

-- 肉料理・居酒屋系
(4,  'images/yakiniku_image_01.jpg', 1),  -- 牛かつ 富田
(10, 'images/yakiniku_image_01.jpg', 1),  -- 豚と炭火 こぶた家
(20, 'images/yakiniku_image_01.jpg', 1),  -- 炭火焼き鳥 鶏尽
(21, 'images/yakiniku_image_01.jpg', 1),  -- 黒毛和牛ごりちゃん心斎橋店
-- たこ焼き
(5,  'images/takoyaki_image_01.jpg', 1),  -- たこ焼き道楽わなか
(46, 'images/takoyaki_image_01.jpg', 1),  -- たこりき
-- ラーメン
(6,  'images/ramen_image_01.jpg', 1),     -- 中華そばふじい
(40, 'images/ramen_image_01.jpg', 1),     -- 老四川紅燒牛肉麺
(45, 'images/ramen_image_01.jpg', 1),     -- 日本橋 さか一
-- 猫カフェ
(7,  'images/cat_image_01.jpg', 1),       -- ねこ浴場＆ねこ旅籠
-- カフェ・イタリアン
(8,  'images/cafe_image_01.jpg', 1),      -- 本宮的茶
(9,  'images/cafe_image_01.jpg', 1),      -- ハーリス なんばマルイ店
(41, 'images/cafe_image_01.jpg', 1),      -- Naga～n cucina italiana
(47, 'images/cafe_image_01.jpg', 1),      -- カフェ＆カレー ボタ
(49, 'images/cafe_image_01.jpg', 1),      -- イルピアット
-- 神社
(11, 'images/shrine_image_01.jpg', 1),    -- 坐摩神社
(12, 'images/shrine_image_02.jpg', 1),    -- サムハラ神社
(13, 'images/shrine_image_03.jpg', 1),    -- 難波八坂神社
-- ガチャポン
(14, 'images/gacha_image_01.jpg', 1),     -- C-pla 大阪心斎橋筋北店
(15, 'images/gacha_image_01.jpg', 1),     -- C-pla 大阪心斎橋筋1丁目店
(16, 'images/gacha_image_01.jpg', 1),     -- C-pla+ 大阪心斎橋筋店
(17, 'images/gacha_image_01.jpg', 1),     -- C-pla 大阪心斎橋筋南店1号館
(18, 'images/gacha_image_01.jpg', 1),     -- C-pla 大阪心斎橋筋南店2号館
-- サウナ
(19, 'images/sauna_image_01.jpg', 1),     -- 想 ‐SOU- SAUNA
-- ライブハウス
(22, 'images/livehouse_image_01.jpg', 1), -- なんばHatch
(23, 'images/livehouse_image_01.jpg', 1), -- Zepp Namba
(25, 'images/livehouse_image_01.jpg', 1), -- 心斎橋BIGCAT
(26, 'images/livehouse_image_01.jpg', 1), -- Music Club JANUS
(27, 'images/livehouse_image_01.jpg', 1), -- OSAKA MUSE
(28, 'images/livehouse_image_01.jpg', 1), -- なんばMele
(29, 'images/livehouse_image_01.jpg', 1), -- 心斎橋SUNHALL
-- ドラッグストア
(30, 'images/pharmacy_image_01.jpg', 1),  -- KoKuMiN クリスタ長堀店
(31, 'images/pharmacy_image_01.jpg', 1),  -- スギ薬局 南船場店
-- スーパー
(32, 'images/supermarget_image_01.jpg', 1), -- 玉出
-- 雑貨屋
(33, 'images/miscgoods_image_01.jpg', 1), -- SARISARI MAMA
-- 外国料理
(34, 'images/ethnicfood_image_01.jpg', 1), -- タワンタイ（タイ料理）
(34, 'images/ethnicfood_image_01.jpg', 2), -- タワンタイ（タイ料理）
(34, 'images/ethnicfood_image_01.jpg', 3), -- タワンタイ（タイ料理）
(36, 'images/ethnicfood_image_01.jpg', 1), -- オーサカバインミー（ベトナム料理）
(38, 'images/ethnicfood_image_01.jpg', 1), -- トルコ料理ナザール
(39, 'images/ethnicfood_image_01.jpg', 1), -- EL PANCHO（メキシコ料理）
-- カレー
(35, 'images/curry_image_01.jpg', 1),     -- ヒマラヤン
(42, 'images/curry_image_01.jpg', 1),     -- 橋本屋
(43, 'images/curry_image_01.jpg', 1),     -- 定食堂 金剛石
-- 韓国料理
(37, 'images/koreanfood_image_01.jpg', 1), -- 韓国料理 HANSSAM
-- 中華料理
(48, 'images/chinesefoods_image_01.jpg', 1), -- 中国料理 艶家
-- ハンバーガー
(44, 'images/burgershop_image_01.jpg', 1), -- CRITTERS BURGER
-- 公園
(50, 'images/park_image_01.jpg', 1),      -- 御津公園（三角公園）
(51, 'images/park_image_01.jpg', 1),      -- 高津公園
(52, 'images/park_image_01.jpg', 1);      -- 道仁公園



