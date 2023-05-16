CREATE TABLE IF NOT EXISTS category_product ( category_product_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
category_product_cd varchar(100) GENERATED ALWAYS AS (REPLACE(SUBSTRING(category_product_name, 1, 100), ' ', '_')),
category_product_name varchar(1000) NOT NULL );
CREATE TABLE IF NOT EXISTS client (
  client_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  client_cd varchar(100) GENERATED ALWAYS AS (SUBSTRING(CONCAT(REPLACE(client_fio, ' ', '_'), '_', client_tel), 1, 100)),
  client_fio varchar(1000) NOT NULL,
  client_tel varchar(20) NOT NULL,
  client_telegram varchar(30),
  client_address varchar(1000),
  client_email varchar(1000)
);
CREATE TABLE IF NOT EXISTS measure_unit ( measure_unit_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
measure_unit_cd varchar(100) GENERATED ALWAYS AS (REPLACE(SUBSTRING(measure_unit_name, 1, 100), ' ', '_')),
measure_unit_name varchar(1000) NOT NULL );
CREATE TABLE IF NOT EXISTS product ( product_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
product_cd varchar(100) GENERATED ALWAYS AS (REPLACE(SUBSTRING(product_name, 1, 100), ' ', '_')),
	product_name varchar(1000) NOT NULL,
	product_category_id BIGINT NOT NULL);
CREATE TABLE IF NOT EXISTS price_history (price_history_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
price_history_product_id BIGINT NOT NULL,
price_history_sum decimal(11, 2) DEFAULT 0,
	price_history_measure_unit_id BIGINT NOT NULL,
	price_history_dt_beg date NOT NULL DEFAULT (CURRENT_DATE),
	price_history_dt_end date NOT NULL DEFAULT (CURRENT_DATE));
--	price_history_dt_end date NOT NULL DEFAULT (CURRENT_DATE));
CREATE TABLE IF NOT EXISTS order_plan (order_plan_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	order_plan_client_id BIGINT NOT NULL,
	order_plan_price_history_id BIGINT NOT NULL,
	order_plan_product_count decimal(11, 2) NOT NULL DEFAULT 1,
	order_plan_dt_beg date NOT NULL DEFAULT (CURRENT_DATE),
	order_plan_dt_end date NOT NULL DEFAULT (CURRENT_DATE),
	order_plan_note varchar(1000),
	order_plan_status TINYINT DEFAULT 0,
	order_plan_ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS discount (discount_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
discount_cd varchar(100) GENERATED ALWAYS AS (REPLACE(SUBSTRING(discount_name, 1, 100), ' ', '_')),
discount_name varchar(1000) NOT NULL ,
discount_percent decimal(11, 2) DEFAULT 0 NOT NULL,
discount_fixed decimal(11, 2) DEFAULT 0 NOT NULL
);
CREATE TABLE IF NOT EXISTS client_discount (client_discount_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	client_discount_client_id BIGINT NOT NULL,
	client_discount_discount_id BIGINT NOT NULL,
	client_discount_dt_beg date NOT NULL DEFAULT (CURRENT_DATE),
	client_discount_dt_end date NOT NULL DEFAULT (CURRENT_DATE)
);
CREATE VIEW order_plan_usr
AS
select client.client_fio, client.client_address, client.client_tel, product.product_name,
order_plan.order_plan_product_count, measure_unit.measure_unit_name,
price_history.price_history_sum as price,
(order_plan.order_plan_product_count * price_history.price_history_sum * (1 - IFNULL(discount.discount_percent, 0) / 100)
- IFNULL(discount.discount_fixed, 0) ) as sum,
order_plan.order_plan_dt_beg,
order_plan.order_plan_dt_end,
order_plan.order_plan_note,
CASE
    WHEN order_plan.order_plan_status = 0 THEN 'In process'
    WHEN order_plan.order_plan_status = 1 THEN 'Done'
    ELSE 'Error'
END as status
,
order_plan.order_plan_status,
order_plan.order_plan_id,
order_plan.order_plan_ts,
CONCAT('Заказ_', client.client_fio, '_', IFNULL(client.client_address, '0'), '_', client.client_tel, '_', product.product_name, '_', order_plan.order_plan_product_count, '_', measure_unit.measure_unit_name, '_',
 order_plan.order_plan_dt_beg, '_', order_plan.order_plan_dt_end, '_', order_plan.order_plan_ts) as summary
from order_plan
join client on order_plan_client_id = client.client_id
join price_history on order_plan.order_plan_price_history_id = price_history.price_history_id
join product on price_history.price_history_product_id = product.product_id
join measure_unit on price_history.price_history_measure_unit_id = measure_unit.measure_unit_id
left join client_discount on client_discount.client_discount_client_id = client.client_id and CURRENT_DATE BETWEEN client_discount.client_discount_dt_beg and client_discount.client_discount_dt_end
left join discount on client_discount.client_discount_discount_id = discount.discount_id;
