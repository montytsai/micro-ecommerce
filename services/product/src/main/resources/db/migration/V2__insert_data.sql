-- 1. 清理舊資料 (for 測試環境)
TRUNCATE TABLE product CASCADE;
TRUNCATE TABLE category CASCADE;

-- 2. 插入 5 個分類
INSERT INTO category (id, name, description, version, created_at, updated_at)
VALUES (1, 'Smartphones', 'Mobile devices and cellular phones', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (2, 'Laptops', 'Portable personal computers', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (3, 'Wearables', 'Smartwatches and fitness trackers', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (4, 'Audio', 'Headphones and speakers', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (5, 'Accessories', 'Chargers, cables, and cases', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 3. 為每個分類插入 10 個商品
INSERT INTO product (id, name, description, available_quantity, price, category_id, version, created_at, updated_at)
SELECT gs.i,                                                   -- id
       c.name || ' Product ' || ((gs.i - 1) % 10 + 1),         -- name (e.g.: XXX Product 1)
       'High quality ' || c.name || ' item',                   -- description
       (CASE WHEN gs.i % 5 = 0 THEN 5.0000 ELSE 100.0000 END), -- available_quantity
       (100.00 + (gs.i * 10.5)),                               -- price (dynamic)
       c.id,                                                   -- category_id
       0,                                                      -- version
       CURRENT_TIMESTAMP,
       CURRENT_TIMESTAMP
FROM generate_series(1, 50) AS gs(i)
         JOIN category c ON c.id = ((gs.i - 1) / 10 + 1);

-- 4. 同步 Sequence
SELECT setval('category_seq', 51);
SELECT setval('product_seq', 101);