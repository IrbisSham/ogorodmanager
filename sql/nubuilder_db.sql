ALTER TABLE product drop CONSTRAINT fk_product_category;

ALTER TABLE price_history drop CONSTRAINT fk_price_history_measure;

ALTER TABLE credit drop CONSTRAINT fk_credit_type;

ALTER TABLE debet drop CONSTRAINT fk_debet_type;

ALTER TABLE order_plan drop CONSTRAINT fk_order_plan_client;

ALTER TABLE order_plan drop CONSTRAINT fk_order_plan_product;

ALTER TABLE order_plan drop CONSTRAINT fk_order_plan_measure_unit_id;

ALTER TABLE order_fact drop CONSTRAINT fk_order_fact_client;

ALTER TABLE order_fact drop CONSTRAINT fk_order_fact_product;

ALTER TABLE order_fact drop CONSTRAINT fk_order_fact_measure_unit_id;

DROP TABLE if EXISTS `client`;
DROP TABLE if EXISTS `category_product`;
DROP TABLE if EXISTS `measure_unit`;
DROP TABLE if EXISTS `product`;
DROP TABLE if EXISTS `price_history`;
DROP TABLE if EXISTS `credit_type`;
DROP TABLE if EXISTS `credit`;
DROP TABLE if EXISTS `debet_type`;
DROP TABLE if EXISTS `debet`;
DROP TABLE if EXISTS `order_plan`;
DROP TABLE if EXISTS `order_fact`;
DROP TABLE if EXISTS `discount`;
DROP TABLE if EXISTS `client_discount`;
DROP TABLE if EXISTS `storage_type`;
DROP TABLE if EXISTS `storage`;

CREATE TABLE `client` (
  `client_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `client_cd` varchar(100) GENERATED ALWAYS AS (SUBSTRING(CONCAT(REPLACE(client_fio, ' ', '_'), '_', client_tel), 1, 100)) STORED,
  `client_fio` text NOT NULL,
  `client_tel` varchar(20) NOT NULL,
  `client_telegram` varchar(30),
  `client_address` varchar(1000),
  `client_email` varchar(1000)
);
create unique index client_cd_idx on client (client_cd);

CREATE TABLE `category_product` ( `category_product_id` BIGINT AUTO_INCREMENT PRIMARY KEY, 
`category_product_cd` varchar(100) GENERATED ALWAYS AS (REPLACE(SUBSTRING(category_product_name, 1, 100), ' ', '_')) STORED, 
`category_product_name` varchar(1000) NOT NULL );
create unique index category_product_cd_idx on category_product (category_product_cd);

CREATE TABLE `measure_unit` ( `measure_unit_id` BIGINT AUTO_INCREMENT PRIMARY KEY, 
`measure_unit_cd` varchar(100) GENERATED ALWAYS AS (REPLACE(SUBSTRING(measure_unit_name, 1, 100), ' ', '_')) STORED, 
`measure_unit_name` varchar(1000) NOT NULL );
create unique index measure_unit_cd_idx on measure_unit (measure_unit_cd);

CREATE TABLE `product` ( `product_id` BIGINT AUTO_INCREMENT PRIMARY KEY, 
`product_cd` varchar(100) GENERATED ALWAYS AS (REPLACE(SUBSTRING(product_name, 1, 100), ' ', '_')) STORED, 
	`product_name` varchar(1000) NOT NULL, 
	`product_category_id` BIGINT NOT NULL);
create unique index product_cd_idx on product (product_cd);

CREATE TABLE `price_history` (`price_history_id` BIGINT AUTO_INCREMENT PRIMARY KEY, 
`price_history_product_id` BIGINT NOT NULL, 
`price_history_sum` float(11, 2) DEFAULT 0,
	price_history_measure_unit_id BIGINT NOT NULL,
	price_history_dt_beg date NOT NULL DEFAULT (now()), 
	price_history_dt_end date NOT NULL DEFAULT (now() + INTERVAL 1 YEAR));
create unique index price_history_product_cd_idx on price_history (price_history_product_cd);
create unique index price_history_product_measure_dt_beg_end_idx on price_history (price_history_product_id, price_history_measure_unit_id, price_history_dt_beg, price_history_dt_end);

CREATE VIEW price_history_usr
AS
select product.product_name, measure_unit.measure_unit_name, 
price_history.price_history_sum as price,
price_history.price_history_id
from price_history join product on price_history.price_history_product_id = product.product_id 
join measure_unit on price_history.price_history_measure_unit_id = measure_unit.measure_unit_id;

CREATE TABLE `credit_type` (`credit_type_id` BIGINT AUTO_INCREMENT PRIMARY KEY, 
`credit_type_cd` varchar(100) GENERATED ALWAYS AS (REPLACE(SUBSTRING(credit_type_name, 1, 100), ' ', '_')) STORED, 
`credit_type_name` varchar(1000) NOT NULL );
create unique index credit_type_cd_idx on credit_type (credit_type_cd);

CREATE TABLE `credit` (`credit_id` BIGINT AUTO_INCREMENT PRIMARY KEY, 
	`credit_type_id` BIGINT NOT NULL,
	`credit_sum` float(11, 2) DEFAULT 0,
	`credit_client_id` BIGINT,
	`credit_dt` date NOT NULL DEFAULT (now()),
	`credit_note` varchar(1000)	
	);
create index credit_type_id_idx on credit (credit_type_id);
create index credit_client_id_idx on credit (credit_client_id);

CREATE TABLE `debet_type` (`debet_type_id` BIGINT AUTO_INCREMENT PRIMARY KEY, 
`debet_type_cd` varchar(100) GENERATED ALWAYS AS (REPLACE(SUBSTRING(debet_type_name, 1, 100), ' ', '_')) STORED, 
`debet_type_name` varchar(1000) NOT NULL );
create unique index debet_type_cd_idx on debet_type (debet_type_cd);

CREATE TABLE `debet` (`debet_id` BIGINT AUTO_INCREMENT PRIMARY KEY, 
	`debet_type_id` BIGINT NOT NULL,
	`debet_sum` float(11, 2) DEFAULT 0,
	`debet_client_id` BIGINT,
	`debet_product_id` BIGINT,
	`debet_product_count` float(11, 2),
	`debet_measure_unit_id` BIGINT,
	`debet_dt` date NOT NULL DEFAULT (now()),
	`debet_note` varchar(1000)	
	);
create index debet_type_id_idx on debet (debet_type_id);
create index debet_client_id_idx on debet (debet_client_id);
create index debet_product_id_idx on debet (debet_product_id);
create index debet_measure_id_idx on debet (debet_measure_unit_id);

CREATE TABLE `order_plan` (`order_plan_id` BIGINT AUTO_INCREMENT PRIMARY KEY, 
	`order_plan_client_id` BIGINT NOT NULL,
	`order_plan_product_id` BIGINT NOT NULL,
	`order_plan_product_count` float(11, 2) NOT NULL DEFAULT 1,
	`order_plan_dt_beg` date NOT NULL DEFAULT (now()), 
	`order_plan_dt_end` date NOT NULL DEFAULT (now() + INTERVAL 1 YEAR),
	`order_plan_note` varchar(1000),
	`order_plan_status` TINYINT DEFAULT 0,
	`order_plan_ts` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
	);
	
create unique index order_plan_client_price_dt_beg_end_idx on order_plan (order_plan_client_id, order_plan_price_history_id, order_plan_dt_beg, order_plan_dt_end);

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
    WHEN order_plan.order_plan_status = 0 THEN "В процессе"
    WHEN order_plan.order_plan_status = 1 THEN "Завершен"
    WHEN order_plan.order_plan_status = 2 THEN "Неактивен"
    ELSE "Ошибка"
END as status
,
order_plan.order_plan_status,
order_plan.order_plan_id,
order_plan.order_plan_ts,
CONCAT('Заказ_', client.client_fio, '_', IFNULL(client.client_address, '0'), '_', client.client_tel, '_', product.product_name, '_', order_plan.order_plan_product_count, '_', measure_unit.measure_unit_name, '_',
 order_plan.order_plan_dt_beg, '_', order_plan.order_plan_dt_end) as summary
from order_plan 
join client on order_plan_client_id = client.client_id 
join price_history on order_plan.order_plan_price_history_id = price_history.price_history_id 
join product on price_history.price_history_product_id = product.product_id 
join measure_unit on price_history.price_history_measure_unit_id = measure_unit.measure_unit_id
left join client_discount on client_discount.client_discount_client_id = client.client_id and CURRENT_DATE BETWEEN client_discount.client_discount_dt_beg and client_discount.client_discount_dt_end
left join discount on client_discount.client_discount_discount_id = discount.discount_id
;

CREATE TABLE `discount` (`discount_id` BIGINT AUTO_INCREMENT PRIMARY KEY, 
`discount_cd` varchar(100) GENERATED ALWAYS AS (REPLACE(SUBSTRING(discount_name, 1, 100), ' ', '_')) STORED, 
`discount_name` varchar(1000) NOT NULL ,
`discount_percent` float(11, 2) DEFAULT 0 NOT NULL,
`discount_fixed` float(11, 2) DEFAULT 0 NOT NULL
);
create unique index discount_cd_idx on discount (discount_cd);

CREATE TABLE `client_discount` (`client_discount_id` BIGINT AUTO_INCREMENT PRIMARY KEY, 
	`client_discount_client_id` BIGINT NOT NULL,
	`client_discount_discount_id` BIGINT NOT NULL,
	`client_discount_dt_beg` date NOT NULL DEFAULT (now()), 
	`client_discount_dt_end` date NOT NULL DEFAULT (now() + INTERVAL 1 YEAR)	
	);
create unique index client_discount_client_discount_dt_beg_end_idx on client_discount (client_discount_client_id, client_discount_discount_id, client_discount_dt_beg, client_discount_dt_end);	

CREATE TABLE `storage_type` (`storage_type_id` BIGINT AUTO_INCREMENT PRIMARY KEY, 
`storage_type_cd` varchar(100) GENERATED ALWAYS AS (REPLACE(SUBSTRING(storage_type_name, 1, 100), ' ', '_')) STORED, 
`storage_type_name` varchar(1000) NOT NULL );
create unique index storage_type_cd_idx on storage_type (storage_type_cd);

CREATE TABLE `storage` (`storage_id` BIGINT AUTO_INCREMENT PRIMARY KEY, 
	`storage_product_id` BIGINT NOT NULL,
	`storage_storage_type_id` BIGINT NOT NULL,
	`storage_product_count` float(11, 2) DEFAULT 0,
	`storage_measure_unit_id` BIGINT NOT NULL,
	`storage_dt_beg` date NOT NULL DEFAULT (now()), 
	`storage_dt_end` date NOT NULL DEFAULT (now() + INTERVAL 1 YEAR),
	`storage_note` varchar(1000)	
	);
create unique index storage_product_type_measure_dt_beg_end_idx on storage (storage_product_id, storage_storage_type_id, storage_measure_unit_id, storage_dt_beg, storage_dt_end);	
create index storage_type_id_idx on storage (storage_storage_type_id);
create index storage_product_id_idx on storage (storage_product_id);
create index storage_measure_id_idx on storage (storage_measure_unit_id);

CREATE TABLE `seed_plant` (`seed_plant_id` BIGINT AUTO_INCREMENT PRIMARY KEY, 
	`seed_plant_product_id` BIGINT,
	`seed_plant_storage_type_id` BIGINT NOT NULL,
	`seed_plant_product_count` float(11, 2) DEFAULT 0,
	`seed_plant_measure_unit_id` BIGINT,
	`seed_plant_dt` date NOT NULL DEFAULT (now()), 
	`seed_plant_note` varchar(1000)	
	);
create unique index seed_plant_product_storage_type_dt_idx on seed_plant (seed_plant_product_id, seed_plant_storage_type_id, seed_plant_dt);	
create index seed_plant_type_id_idx on seed_plant (seed_plant_storage_type_id);
create index seed_plant_product_id_idx on seed_plant (seed_plant_product_id);
create index seed_plant_measure_id_idx on seed_plant (seed_plant_measure_unit_id);

ALTER TABLE product
    ADD CONSTRAINT fk_product_category FOREIGN KEY
    (product_category_id)
    REFERENCES category_product (category_product_id);
	
ALTER TABLE price_history
    ADD CONSTRAINT fk_price_history_product FOREIGN KEY
    (price_history_product_id)
    REFERENCES product (product_id);	

ALTER TABLE price_history
    ADD CONSTRAINT fk_price_history_measure FOREIGN KEY
    (price_history_measure_unit_id)
    REFERENCES measure_unit (measure_unit_id);	

ALTER TABLE credit
    ADD CONSTRAINT fk_credit_type FOREIGN KEY
    (credit_type_id)
    REFERENCES credit_type (credit_type_id);
	
ALTER TABLE credit
    ADD CONSTRAINT fk_credit_client FOREIGN KEY
    (credit_client_id)
    REFERENCES client (client_id);		
	
ALTER TABLE debet
    ADD CONSTRAINT fk_debet_type FOREIGN KEY
    (debet_type_id)
    REFERENCES debet_type (debet_type_id);	

ALTER TABLE debet
    ADD CONSTRAINT fk_debet_client FOREIGN KEY
    (debet_client_id)
    REFERENCES client (client_id);	
	
ALTER TABLE debet
    ADD CONSTRAINT fk_debet_product FOREIGN KEY
    (debet_product_id)
    REFERENCES product (product_id);	
	
ALTER TABLE debet
    ADD CONSTRAINT fk_debet_measure FOREIGN KEY
    (debet_measure_unit_id)
    REFERENCES measure_unit (measure_unit_id);	
	
ALTER TABLE client_discount
    ADD CONSTRAINT fk_client_discount FOREIGN KEY
    (client_discount_client_id)
    REFERENCES client (client_id);	
	
ALTER TABLE client_discount
    ADD CONSTRAINT fk_client_discount_id FOREIGN KEY
    (client_discount_id)
    REFERENCES discount (discount_id);	
	
ALTER TABLE order_plan
    ADD CONSTRAINT fk_order_plan_client FOREIGN KEY
    (order_plan_client_id)
    REFERENCES client (client_id);	
	
ALTER TABLE order_plan
    ADD CONSTRAINT fk_order_plan_price_history FOREIGN KEY
    (order_plan_price_history_id)
    REFERENCES price_history (price_history_id);	
	
ALTER TABLE storage
    ADD CONSTRAINT fk_storage_type FOREIGN KEY
    (storage_storage_type_id)
    REFERENCES storage_type (storage_type_id);		
	
ALTER TABLE storage
    ADD CONSTRAINT fk_storage_product FOREIGN KEY
    (storage_product_id)
    REFERENCES product (product_id);		
	
ALTER TABLE storage
    ADD CONSTRAINT fk_storage_measure FOREIGN KEY
    (storage_measure_unit_id)
    REFERENCES measure_unit (measure_unit_id);	
	
ALTER TABLE seed_plant
    ADD CONSTRAINT fk_seed_plant_type FOREIGN KEY
    (seed_plant_storage_type_id)
    REFERENCES storage_type (storage_type_id);		
	
ALTER TABLE seed_plant
    ADD CONSTRAINT fk_seed_plant_product FOREIGN KEY
    (seed_plant_product_id)
    REFERENCES product (product_id);		
	
ALTER TABLE seed_plant
    ADD CONSTRAINT fk_seed_plant_measure FOREIGN KEY
    (seed_plant_measure_unit_id)
    REFERENCES measure_unit (measure_unit_id);