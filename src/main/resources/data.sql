insert into users(username,password,email,role,display_name)
values('admin', 'pass', 'a@nagahori.com', 'ADMIN', '管理者');
insert into users(username,password,email,role,display_name)
values('佐藤花子', 'aaab', 'b@nagahori.com', 'USER', 'ユーザー1');
insert into users(username,password,email,role,display_name)
values('山本次郎', 'aaac', 'c@nagahori.com', 'USER', 'ユーザー2');
insert into users(username,password,email,role,display_name)
values('佐々木三郎', 'aaad', 'd@nagahori.com', 'USER', 'ユーザー3');
insert into users(username,password,email,role,display_name)
values('鈴木四郎', 'aaae', 'e@nagahori.com', 'USER', 'ユーザー4');
insert into users(username,password,email,role,display_name)
values('木村五郎', 'aaaf', 'f@nagahori.com', 'USER', 'ユーザー5');


insert into spots(spot_name,address,business_hours,website_url,keywords,closed_days,estimated_budget,details)
values('なんばグランド花月','542-0075大阪府大阪市中央区難波千日前11-6','10:00～22:00','https://ngk.yoshimoto.co.jp/','劇場','不定休','3000円～','新喜劇と漫才が楽しめる笑いの殿堂');
insert into spots(spot_name,address,business_hours,website_url,keywords,closed_days,estimated_budget,details)
values('よしもと漫才劇場','542-0075大阪府大阪市中央区難波千日前12-7 YES-NAMBAビル5F','10:00～22:00','https://manzaigekijyo.yoshimoto.co.jp/','劇場','不定休','1300円～','次世代のお笑いスターを発掘できる');
insert into spots(spot_name,address,business_hours,website_url,keywords,closed_days,estimated_budget,details)
values('よしもと道頓堀シアター','542-0071大阪府大阪市中央区道頓堀1-7-21 中座くいだおれビル 6F','10:00～22:00','https://dotonbori.yoshimoto.co.jp/','劇場','不定休','2000円～','食べて笑って楽しめる英語対応お笑い劇場');
insert into spots(spot_name,address,business_hours,website_url,keywords,closed_days,estimated_budget,details)
values('牛かつ　富田','〒226-0011大阪府大阪市浪速区難波中2-3-1 2F','11:00～23:00','なし','肉','不定休','1500円～','自分好みに焼いて楽しむ、体験型の牛かつ専門店');
insert into spots(spot_name,address,business_hours,website_url,keywords,closed_days,estimated_budget,details)
values('たこ焼き道楽わなか千日前本店','542-0075大阪府大阪市中央区難波千日前11-19','(月～金) 10:30～21:00(土・日・祝) 9:30～21:00
','https://ngk.yoshimoto.co.jp/','粉もん','なし','500円～','外はカリッ、中はトロッ！大阪名物たこ焼きの老舗');
insert into spots(spot_name,address,business_hours,website_url,keywords,closed_days,estimated_budget,details)
values('中華そばふじい難波千日前店','542-0076大阪府大阪市中央区難波1-3-14','11:00～23:30','https://ra-men.co/','ラーメン','火曜日','昼 900円～/夜 1000円～','昔懐かしい醤油ベースの中華そばに背脂のコクが効いた、大阪名物ラーメン');

insert into categories(name,display_order)
values('グルメ',1);
insert into categories(name,display_order)
values('観光スポット',2);
insert into categories(name,display_order)
values('ショッピング',3);
insert into categories(name,display_order)
values('娯楽',4);
insert into categories(name,display_order)
values('カフェ',5);
insert into categories(name,display_order)
values('その他',6);


insert into sub_categories(category_id,name)
values(1,'居酒屋');
insert into sub_categories(category_id,name)
values(1,'外国料理');
insert into sub_categories(category_id,name)
values(4,'ライブハウス');
insert into sub_categories(category_id,name)
values(3,'ガチャポン');
insert into sub_categories(category_id,name)
values(2,'神社');
insert into sub_categories(category_id,name)
values(4,'劇場');
insert into sub_categories(category_id,name)
values(5,'カフェ');
insert into sub_categories(category_id,name)
values(1,'カレー');
insert into sub_categories(category_id,name)
values(1,'ラーメン');
insert into sub_categories(category_id,name)
values(1,'たこ焼き');
insert into sub_categories(category_id,name)
values(4,'サウナ');
insert into sub_categories(category_id,name)
values(4,'猫カフェ');
insert into sub_categories(category_id,name)
values(3,'ドラッグストア');
insert into sub_categories(category_id,name)
values(3,'スーパー');
insert into sub_categories(category_id,name)
values(1,'イタリアン');
insert into sub_categories(category_id,name)
values(1,'ハンバーガー');
insert into sub_categories(category_id,name)
values(3,'雑貨屋');
insert into sub_categories(category_id,name)
values(1,'韓国料理');
insert into sub_categories(category_id,name)
values(1,'中華料理');
insert into sub_categories(category_id,name)
values(3,'100円ショップ');
insert into sub_categories(category_id,name)
values(6,'その他');


insert into reviews(user_id,spot_id,rating,comment)
values(1,1,1,'テストレビュー:不味かった。二度と行かない。');
insert into reviews(user_id,spot_id,rating,comment)
values(2,2,5,'美味しかった');
insert into reviews(user_id,spot_id,rating,comment)
values(3,3,5,'面白かった');


insert into favorites(user_id,spot_id)
values(1,1);
insert into favorites(user_id,spot_id)
values(2,2);
insert into favorites(user_id,spot_id)
values(3,3);
insert into favorites(user_id,spot_id)
values(4,4);


insert into spot_photos(spot_id,photo_url,display_order)
values(1,'画像パス',1),
          (1,'画像パス',2),
          (1,'画像パス',3);