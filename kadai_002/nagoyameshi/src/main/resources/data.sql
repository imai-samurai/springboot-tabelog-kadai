-- 1. ロール（roles）の初期データ
INSERT IGNORE INTO roles (id, name) VALUES (1, 'ROLE_FREE');
INSERT IGNORE INTO roles (id, name) VALUES (2, 'ROLE_PREMIUM');

-- 5. カテゴリ（categories）のテストデータ
INSERT IGNORE INTO categories (id, name) VALUES (1, '焼肉');
INSERT IGNORE INTO categories (id, name) VALUES (2, 'ラーメン');
INSERT IGNORE INTO categories (id, name) VALUES (3, '寿司');
INSERT IGNORE INTO categories (id, name) VALUES (4, 'ひつまぶし');
INSERT IGNORE INTO categories (id, name) VALUES (5, '手羽先');
INSERT IGNORE INTO categories (id, name) VALUES (6, '味噌カツ');
INSERT IGNORE INTO categories (id, name) VALUES (7, 'カレー');
INSERT IGNORE INTO categories (id, name) VALUES (8, '牛丼');
INSERT IGNORE INTO categories (id, name) VALUES (9, '居酒屋');
INSERT IGNORE INTO categories (id, name) VALUES (10, '揚げ物');
INSERT IGNORE INTO categories (id, name) VALUES (11, '海鮮');
INSERT IGNORE INTO categories (id, name) VALUES (12, '喫茶店');
INSERT IGNORE INTO categories (id, name) VALUES (13, 'きしめん');
INSERT IGNORE INTO categories (id, name) VALUES (14, '台湾ラーメン');
INSERT IGNORE INTO categories (id, name) VALUES (15, 'あんかけスパ');
INSERT IGNORE INTO categories (id, name) VALUES (16, 'イタリアン');
INSERT IGNORE INTO categories (id, name) VALUES (17, 'フレンチ');
INSERT IGNORE INTO categories (id, name) VALUES (18, '中華料理');
INSERT IGNORE INTO categories (id, name) VALUES (19, 'スイーツ');
INSERT IGNORE INTO categories (id, name) VALUES (20, '和菓子');
INSERT IGNORE INTO categories (id, name) VALUES (21, 'バー');
INSERT IGNORE INTO categories (id, name) VALUES (22, 'うどん');
INSERT IGNORE INTO categories (id, name) VALUES (23, 'そば');
INSERT IGNORE INTO categories (id, name) VALUES (24, 'ステーキ');
INSERT IGNORE INTO categories (id, name) VALUES (25, 'ハンバーグ');


-- 6. 店舗情報（stores）のテストデータ（category_id カラムを消去しました）
INSERT IGNORE INTO stores (id, name, image_name, description, price_upper, price_lower, hours_open, hours_close, postal_code, address, phone_number, regular_holiday) 
VALUES (1, '名古屋 炭火焼肉ひがし', 'store_01.jpg', '厳選された上質な飛騨牛を炭火焼きで堪能できる本格焼肉店です。', 8000, 4000, '17:00:00', '23:00:00', '460-0008', '愛知県名古屋市中区栄1-1-1', '052-111-2222', '月曜日');

INSERT IGNORE INTO stores (id, name, image_name, description, price_upper, price_lower, hours_open, hours_close, postal_code, address, phone_number, regular_holiday) 
VALUES (2, '尾張 濃厚味噌らーめん', 'store_02.jpg', 'じっくり煮込んだコク深い極上味噌スープと自家製麺が絡む一杯。', 1500, 900, '11:00:00', '22:00:00', '450-0002', '愛知県名古屋市中村区名駅2-2-2', '052-333-4444', '年中無休');

INSERT IGNORE INTO stores (id, name, image_name, description, price_upper, price_lower, hours_open, hours_close, postal_code, address, phone_number, regular_holiday) 
VALUES (3, '大須 寿司政', 'store_03.jpg', '毎朝柳橋中央市場から仕入れる新鮮なネタを職人が目の前で握ります。', 6000, 2000, '11:30:00', '21:30:00', '460-0011', '愛知県名古屋市中区大須3-3-3', '052-555-6666', '水曜日');

INSERT IGNORE INTO stores (id, name, image_name, description, price_upper, price_lower, hours_open, hours_close, postal_code, address, phone_number, regular_holiday) 
VALUES (4, '名古屋 鰻乃家', 'store_04.jpg', '伝統の秘伝タレで香ばしく焼き上げる絶品のひつまぶし専門店です。', 5000, 3000, '11:00:00', '21:00:00', '460-0008', '愛知県名古屋市中区栄1-2-3', '052-123-4567', '木曜日');

INSERT IGNORE INTO stores (id, name, image_name, description, price_upper, price_lower, hours_open, hours_close, postal_code, address, phone_number, regular_holiday) 
VALUES (5, '手羽先居酒屋 風の鳥', 'store_05.jpg', '特製スパイスが効いたピリ辛手羽先は外はパリパリ、中はジューシー！', 3000, 1500, '17:00:00', '23:30:00', '450-0002', '愛知県名古屋市中村区名駅7-8-9', '052-555-5555', '年中無休');

INSERT IGNORE INTO stores (id, name, image_name, description, price_upper, price_lower, hours_open, hours_close, postal_code, address, phone_number, regular_holiday) 
VALUES (6, '元祖みそカツ 芳味亭', 'store_06.jpg', '濃厚な自家製味噌ダレがサクサクの衣に染み込んだ名古屋伝統の味。', 2000, 1000, '11:30:00', '20:30:00', '460-0003', '愛知県名古屋市中区錦4-5-6', '052-987-6543', '火曜日');

INSERT IGNORE INTO stores (id, name, image_name, description, price_upper, price_lower, hours_open, hours_close, postal_code, address, phone_number, regular_holiday) 
VALUES (7, '金鯱 スパイスカレー', 'store_07.jpg', '名古屋の食材を隠し味に使った、奥深いコクのこだわりカレー。', 1500, 1000, '11:00:00', '20:00:00', '460-0002', '愛知県名古屋市中区丸の内5-5-5', '052-777-8888', '日曜日');

INSERT IGNORE INTO stores (id, name, image_name, description, price_upper, price_lower, hours_open, hours_close, postal_code, address, phone_number, regular_holiday) 
VALUES (8, '丸八 牛丼本舗', 'store_08.jpg', '特製の出汁でじっくり煮込んだ、どこか懐かしい味わいの牛丼です。', 1000, 600, '10:00:00', '22:00:00', '450-0003', '愛知県名古屋市中村区名駅南6-6-6', '052-999-0000', '年中無休');

INSERT IGNORE INTO stores (id, name, image_name, description, price_upper, price_lower, hours_open, hours_close, postal_code, address, phone_number, regular_holiday) 
VALUES (9, '駿河屋 手打ちうどん', 'store_09.jpg', '本場仕込みのコシの強さと、カツオ出汁の香りが引き立つ極上の手打ちうどん。', 1200, 700, '11:00:00', '20:00:00', '460-0001', '愛知県名古屋市中区三の丸1-1-1', '052-123-0009', '月曜日');

INSERT IGNORE INTO stores (id, name, image_name, description, price_upper, price_lower, hours_open, hours_close, postal_code, address, phone_number, regular_holiday) 
VALUES (10, '信州そば処 葵', 'store_10.jpg', '挽きたて・打ちたて・茹でたての「三たて」にこだわった香り高い本格蕎麦。', 1800, 900, '11:30:00', '21:00:00', '450-0003', '愛知県名古屋市中村区名駅南3-3-3', '052-123-0010', '火曜日');

INSERT IGNORE INTO stores (id, name, image_name, description, price_upper, price_lower, hours_open, hours_close, postal_code, address, phone_number, regular_holiday) 
VALUES (11, '純喫茶 メロウ', 'store_11.jpg', '銅板でじっくり焼き上げたふわふわ極厚パンケーキが自慢のレトロ喫茶。', 1500, 600, '08:00:00', '18:00:00', '460-0008', '愛知県名古屋市中区栄4-4-4', '052-123-0011', '年中無休');

INSERT IGNORE INTO stores (id, name, image_name, description, price_upper, price_lower, hours_open, hours_close, postal_code, address, phone_number, regular_holiday) 
VALUES (12, '本格中華 祥龍園', 'store_12.jpg', '強火で一気に炒め上げるパラパラの絶品チャーハンと本格中華の数々。', 2500, 800, '11:30:00', '22:00:00', '460-0002', '愛知県名古屋市中区丸の内3-3-3', '052-123-0012', '水曜日');

INSERT IGNORE INTO stores (id, name, image_name, description, price_upper, price_lower, hours_open, hours_close, postal_code, address, phone_number, regular_holiday) 
VALUES (13, '御菓子司 清月堂', 'store_13.jpg', '創業当時から変わらぬ製法で一つ一つ丁寧に仕上げる季節の伝統和菓子。', 1000, 300, '09:00:00', '18:00:00', '460-0011', '愛知県名古屋市中区大須2-2-2', '052-123-0013', '木曜日');

INSERT IGNORE INTO stores (id, name, image_name, description, price_upper, price_lower, hours_open, hours_close, postal_code, address, phone_number, regular_holiday) 
VALUES (14, '手ごねハンバーグ工房', 'store_14.jpg', '肉汁が溢れ出す！和牛100%にこだわった特製デミグラスハンバーグ。', 2200, 1300, '11:00:00', '21:30:00', '464-0075', '愛知県名古屋市千種区内山1-1-1', '052-123-0014', '年中無休');

INSERT IGNORE INTO stores (id, name, image_name, description, price_upper, price_lower, hours_open, hours_close, postal_code, address, phone_number, regular_holiday) 
VALUES (15, '熟成肉ステーキハウス', 'store_15.jpg', '専用庫でじっくり熟成させ、旨味を極限まで凝縮した極厚ステーキ。', 9000, 3500, '17:00:00', '23:00:00', '461-0001', '愛知県名古屋市東区泉1-1-1', '052-123-0015', '第3日曜日');


-- 6.5 店舗とカテゴリの紐付けデータ（中間テーブル store_category への投入）
-- VALUES (店舗のID, カテゴリのID) で紐付け
INSERT IGNORE INTO store_category (store_id, category_id) VALUES (1, 1); -- ひがし × 焼肉
INSERT IGNORE INTO store_category (store_id, category_id) VALUES (2, 2); -- 濃厚味噌らーめん × ラーメン
INSERT IGNORE INTO store_category (store_id, category_id) VALUES (3, 3); -- 寿司政 × 寿司
INSERT IGNORE INTO store_category (store_id, category_id) VALUES (4, 4); -- 鰻乃家 × ひつまぶし
INSERT IGNORE INTO store_category (store_id, category_id) VALUES (5, 5); -- 風の鳥 × 手羽先
INSERT IGNORE INTO store_category (store_id, category_id) VALUES (6, 6); -- 芳味亭 × 味噌カツ
INSERT IGNORE INTO store_category (store_id, category_id) VALUES (7, 7); -- スパイスカレー × カレー
INSERT IGNORE INTO store_category (store_id, category_id) VALUES (8, 8); -- 牛丼本舗 × 牛丼
INSERT IGNORE INTO store_category (store_id, category_id) VALUES (3, 11);  -- 大須 寿司政 × 海鮮
INSERT IGNORE INTO store_category (store_id, category_id) VALUES (5, 9);   -- 手羽先居酒屋 風の鳥 × 居酒屋
INSERT IGNORE INTO store_category (store_id, category_id) VALUES (5, 10);  -- 手羽先居酒屋 風の鳥 × 揚げ物
INSERT IGNORE INTO store_category (store_id, category_id) VALUES (6, 10);  -- 元祖みそカツ 芳味亭 × 揚げ物
INSERT IGNORE INTO store_category (store_id, category_id) VALUES (9, 22);
INSERT IGNORE INTO store_category (store_id, category_id) VALUES (10, 23);
INSERT IGNORE INTO store_category (store_id, category_id) VALUES (11, 12);
INSERT IGNORE INTO store_category (store_id, category_id) VALUES (11, 19);
INSERT IGNORE INTO store_category (store_id, category_id) VALUES (12, 18);
INSERT IGNORE INTO store_category (store_id, category_id) VALUES (13, 20);
INSERT IGNORE INTO store_category (store_id, category_id) VALUES (14, 25);
INSERT IGNORE INTO store_category (store_id, category_id) VALUES (15, 24);

-- 8. レビュー（reviews）のテストデータ
-- 【店舗ID: 1（焼肉店）へのレビュー3件】
INSERT IGNORE INTO reviews (id, store_id, user_id, score, comment) VALUES (1, 1, 1, 5, 'カルビもロースも肉質が非常に高くて驚きました！自家製のタレが絶品で白米が止まらなくなります。特別な日のディナーに最高の焼肉店です。');
INSERT IGNORE INTO reviews (id, store_id, user_id, score, comment) VALUES (2, 1, 1, 4, 'お肉が口の中でとろける美味しさでした。週末の夜はかなり混み合うので、事前にしっかりと枠を確保して訪問するのがおすすめです。');
INSERT IGNORE INTO reviews (id, store_id, user_id, score, comment) VALUES (3, 1, 1, 4, '店内は無煙ロッカーやダクトが優秀で、煙の匂いが服に付きにくく快適に過ごせました。スタッフの方の網交換の目配りも素晴らしかったです。');
-- 【店舗ID: 2（ラーメン店）へのレビュー3件】
INSERT IGNORE INTO reviews (id, store_id, user_id, score, comment) VALUES (4, 2, 1, 5, 'スープのコクと麺の絡みが絶妙で、最後の一滴まで飲み干してしまうほど完成度の高い一杯でした！トッピングのチャーシューもトロトロです。');
INSERT IGNORE INTO reviews (id, store_id, user_id, score, comment) VALUES (5, 2, 1, 4, '濃厚でありながら後味がすっきりとした、ハイレベルなラーメンに大満足です。卓上調味料での味変も楽しく、リピート確定です。');
INSERT IGNORE INTO reviews (id, store_id, user_id, score, comment) VALUES (6, 2, 1, 3, '味は文句なしに美味しいのですが、お昼のピーク時は回転重視で少しバタバタした印象。少し時間をずらして行くのがベストだと思います。');
-- 【店舗ID: 3（寿司店）へのレビュー3件】
INSERT IGNORE INTO reviews (id, store_id, user_id, score, comment) VALUES (7, 3, 1, 5, '職人さんが目の前で握ってくれるネタはどれも新鮮そのもの！特にマグロと地物の地魚が絶品で、シャリの解け加減も完璧でした。');
INSERT IGNORE INTO reviews (id, store_id, user_id, score, comment) VALUES (8, 3, 1, 4, '落ち着いた和の空間でゆっくりと本格的なお寿司を堪能できました。ランチタイムのセットは非常にコスパが高くて大満足です。');
INSERT IGNORE INTO reviews (id, store_id, user_id, score, comment) VALUES (9, 3, 1, 3, '味や鮮度は間違いなく一級品ですが、人気店のため少し席の確保が難しい時間帯があります。大人の隠れ家的な素晴らしいお店です。');

