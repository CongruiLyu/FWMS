-- FWMS 食品仓库管理系统数据库
SET FOREIGN_KEY_CHECKS = 0;
CREATE DATABASE IF NOT EXISTS fwms DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE fwms;

-- 用户表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码',
    `real_name` VARCHAR(50) NOT NULL COMMENT '真实姓名',
    `role` VARCHAR(30) NOT NULL COMMENT '角色: ADMIN/WAREHOUSE/PURCHASER/QC/PRODUCTION/SALES',
    `phone` VARCHAR(20) COMMENT '电话',
    `status` TINYINT DEFAULT 1 COMMENT '1启用 0禁用',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 供应商表
DROP TABLE IF EXISTS `supplier`;
CREATE TABLE `supplier` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `code` VARCHAR(20) NOT NULL UNIQUE COMMENT '供应商编号',
    `name` VARCHAR(100) NOT NULL COMMENT '供应商名称',
    `contact` VARCHAR(50) COMMENT '联系人',
    `phone` VARCHAR(20) COMMENT '电话',
    `address` VARCHAR(200) COMMENT '地址',
    `status` TINYINT DEFAULT 1,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='供应商表';

-- 原料表
DROP TABLE IF EXISTS `material`;
CREATE TABLE `material` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `code` VARCHAR(20) NOT NULL UNIQUE COMMENT '原料编号',
    `name` VARCHAR(100) NOT NULL COMMENT '原料名称',
    `spec` VARCHAR(50) COMMENT '规格',
    `unit` VARCHAR(10) NOT NULL COMMENT '单位',
    `alert_threshold` INT DEFAULT 20 COMMENT '库存预警阈值',
    `status` TINYINT DEFAULT 1,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='原料表';

-- 库位表
DROP TABLE IF EXISTS `warehouse_location`;
CREATE TABLE `warehouse_location` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `code` VARCHAR(20) NOT NULL UNIQUE COMMENT '库位编号',
    `name` VARCHAR(50) COMMENT '库位名称',
    `zone` VARCHAR(10) NOT NULL COMMENT '区域: A原料/B车间/C成品/D发货',
    `zone_name` VARCHAR(50) COMMENT '区域名称',
    `capacity` INT DEFAULT 1000 COMMENT '容量',
    `sort_order` INT DEFAULT 0 COMMENT '排序(交期排序用)',
    `status` TINYINT DEFAULT 1,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库位表';

-- 原料库存表
DROP TABLE IF EXISTS `material_stock`;
CREATE TABLE `material_stock` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `material_id` BIGINT NOT NULL COMMENT '原料ID',
    `location_id` BIGINT COMMENT '库位ID',
    `quantity` DECIMAL(12,2) DEFAULT 0 COMMENT '库存数量',
    `last_inbound_time` DATETIME COMMENT '最近入库时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_material_location` (`material_id`, `location_id`),
    FOREIGN KEY (`material_id`) REFERENCES `material`(`id`),
    FOREIGN KEY (`location_id`) REFERENCES `warehouse_location`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='原料库存表';

-- 采购单表
DROP TABLE IF EXISTS `purchase_order`;
CREATE TABLE `purchase_order` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `order_no` VARCHAR(30) NOT NULL UNIQUE COMMENT '采购单号',
    `supplier_id` BIGINT NOT NULL COMMENT '供应商ID',
    `purchase_date` DATE NOT NULL COMMENT '采购日期',
    `expected_date` DATE COMMENT '预计到货日期',
    `total_amount` DECIMAL(12,2) DEFAULT 0 COMMENT '总金额',
    `status` VARCHAR(20) DEFAULT 'PENDING' COMMENT 'PENDING/ARRIVED/COMPLETED/CANCELLED',
    `purchaser_id` BIGINT COMMENT '采购员ID',
    `remark` VARCHAR(500),
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`supplier_id`) REFERENCES `supplier`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购单表';

-- 采购单明细
DROP TABLE IF EXISTS `purchase_order_item`;
CREATE TABLE `purchase_order_item` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `order_id` BIGINT NOT NULL,
    `material_id` BIGINT NOT NULL,
    `quantity` DECIMAL(12,2) NOT NULL,
    `unit_price` DECIMAL(10,2) DEFAULT 0,
    FOREIGN KEY (`order_id`) REFERENCES `purchase_order`(`id`),
    FOREIGN KEY (`material_id`) REFERENCES `material`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购单明细';

-- 原料入库表
DROP TABLE IF EXISTS `material_inbound`;
CREATE TABLE `material_inbound` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `inbound_no` VARCHAR(30) NOT NULL UNIQUE COMMENT '入库编号',
    `purchase_order_id` BIGINT COMMENT '关联采购单',
    `supplier_id` BIGINT NOT NULL,
    `material_id` BIGINT NOT NULL,
    `quantity` DECIMAL(12,2) NOT NULL,
    `location_id` BIGINT COMMENT '入库库位',
    `qc_result` VARCHAR(20) COMMENT 'PASS/FAIL',
    `qc_inspector_id` BIGINT COMMENT '质检员',
    `operator_id` BIGINT COMMENT '操作员',
    `inbound_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `remark` VARCHAR(500),
    FOREIGN KEY (`supplier_id`) REFERENCES `supplier`(`id`),
    FOREIGN KEY (`material_id`) REFERENCES `material`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='原料入库表';

-- 质检记录表
DROP TABLE IF EXISTS `quality_inspection`;
CREATE TABLE `quality_inspection` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `inspection_no` VARCHAR(30) NOT NULL UNIQUE COMMENT '质检编号',
    `type` VARCHAR(20) NOT NULL COMMENT 'MATERIAL/PRODUCT',
    `ref_id` BIGINT COMMENT '关联入库/生产ID',
    `material_id` BIGINT COMMENT '原料ID',
    `product_id` BIGINT COMMENT '产品ID',
    `quantity` DECIMAL(12,2) NOT NULL,
    `result` VARCHAR(20) NOT NULL COMMENT 'PASS/FAIL/REWORK',
    `inspector_id` BIGINT NOT NULL,
    `inspect_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `remark` VARCHAR(500)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='质检记录表';

-- 领料出库表
DROP TABLE IF EXISTS `material_outbound`;
CREATE TABLE `material_outbound` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `outbound_no` VARCHAR(30) NOT NULL UNIQUE COMMENT '领料单号',
    `workshop` VARCHAR(50) NOT NULL COMMENT '生产车间',
    `applicant_id` BIGINT NOT NULL COMMENT '申请人',
    `material_id` BIGINT NOT NULL,
    `quantity` DECIMAL(12,2) NOT NULL,
    `status` VARCHAR(20) DEFAULT 'PENDING' COMMENT 'PENDING/APPROVED/REJECTED/COMPLETED',
    `approver_id` BIGINT COMMENT '审核人',
    `outbound_time` DATETIME COMMENT '发料时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `remark` VARCHAR(500),
    FOREIGN KEY (`material_id`) REFERENCES `material`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='领料出库表';

-- 产品表
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `code` VARCHAR(20) NOT NULL UNIQUE COMMENT '产品编号',
    `name` VARCHAR(100) NOT NULL COMMENT '产品名称',
    `spec` VARCHAR(50) COMMENT '规格',
    `unit` VARCHAR(10) NOT NULL COMMENT '单位',
    `shelf_life_days` INT DEFAULT 180 COMMENT '保质期(天)',
    `alert_threshold` INT DEFAULT 50 COMMENT '库存预警阈值',
    `status` TINYINT DEFAULT 1,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品表';

-- 生产批次表
DROP TABLE IF EXISTS `production`;
CREATE TABLE `production` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `batch_no` VARCHAR(30) NOT NULL UNIQUE COMMENT '生产批次号',
    `product_id` BIGINT NOT NULL,
    `quantity` DECIMAL(12,2) NOT NULL COMMENT '生产数量',
    `production_date` DATE NOT NULL,
    `leader_id` BIGINT COMMENT '负责人',
    `status` VARCHAR(20) DEFAULT 'PRODUCING' COMMENT 'PRODUCING/QC_PENDING/QC_PASS/QC_FAIL/STORED',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`product_id`) REFERENCES `product`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='生产批次表';

-- 成品库存表
DROP TABLE IF EXISTS `product_stock`;
CREATE TABLE `product_stock` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `product_id` BIGINT NOT NULL,
    `location_id` BIGINT COMMENT '库位ID',
    `quantity` DECIMAL(12,2) DEFAULT 0,
    `production_date` DATE COMMENT '生产日期',
    `expiry_date` DATE COMMENT '保质期至',
    `delivery_date` DATE COMMENT '交货日期(用于库位排序)',
    `last_inbound_time` DATETIME,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`product_id`) REFERENCES `product`(`id`),
    FOREIGN KEY (`location_id`) REFERENCES `warehouse_location`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成品库存表';

-- 客户表
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `code` VARCHAR(20) NOT NULL UNIQUE,
    `name` VARCHAR(100) NOT NULL,
    `contact` VARCHAR(50),
    `phone` VARCHAR(20),
    `address` VARCHAR(200),
    `status` TINYINT DEFAULT 1,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户表';

-- 销售订单表
DROP TABLE IF EXISTS `sales_order`;
CREATE TABLE `sales_order` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `order_no` VARCHAR(30) NOT NULL UNIQUE,
    `customer_id` BIGINT NOT NULL,
    `product_id` BIGINT NOT NULL,
    `quantity` DECIMAL(12,2) NOT NULL,
    `delivery_date` DATE NOT NULL COMMENT '交货日期',
    `status` VARCHAR(20) DEFAULT 'PENDING' COMMENT 'PENDING/PICKING/SHIPPED/COMPLETED/CANCELLED',
    `salesperson_id` BIGINT,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `remark` VARCHAR(500),
    FOREIGN KEY (`customer_id`) REFERENCES `customer`(`id`),
    FOREIGN KEY (`product_id`) REFERENCES `product`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售订单表';

-- 发货出库表
DROP TABLE IF EXISTS `shipment`;
CREATE TABLE `shipment` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `shipment_no` VARCHAR(30) NOT NULL UNIQUE COMMENT '出库单号',
    `sales_order_id` BIGINT NOT NULL,
    `product_id` BIGINT NOT NULL,
    `quantity` DECIMAL(12,2) NOT NULL,
    `location_id` BIGINT COMMENT '出库库位',
    `operator_id` BIGINT,
    `shipment_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `remark` VARCHAR(500),
    FOREIGN KEY (`sales_order_id`) REFERENCES `sales_order`(`id`),
    FOREIGN KEY (`product_id`) REFERENCES `product`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='发货出库表';

-- 库存预警通知表
DROP TABLE IF EXISTS `stock_alert`;
CREATE TABLE `stock_alert` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `type` VARCHAR(20) NOT NULL COMMENT 'MATERIAL/PRODUCT',
    `ref_id` BIGINT NOT NULL,
    `ref_name` VARCHAR(100),
    `current_qty` DECIMAL(12,2),
    `threshold` INT,
    `message` VARCHAR(200),
    `is_read` TINYINT DEFAULT 0,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存预警表';

-- ========== 初始数据 ==========

-- 用户 (密码均为 123456)
INSERT INTO `user` (`username`, `password`, `real_name`, `role`, `phone`) VALUES
('admin', '123456', '系统管理员', 'ADMIN', '13800000001'),
('warehouse', '123456', '王仓管', 'WAREHOUSE', '13800000002'),
('purchaser', '123456', '李采购', 'PURCHASER', '13800000003'),
('qc', '123456', '张质检', 'QC', '13800000004'),
('production', '123456', '赵生产', 'PRODUCTION', '13800000005'),
('sales', '123456', '刘销售', 'SALES', '13800000006');

-- 库位
INSERT INTO `warehouse_location` (`code`, `name`, `zone`, `zone_name`, `capacity`, `sort_order`) VALUES
('A01', '面粉库位', 'A', '原料仓', 500, 1),
('A02', '白糖库位', 'A', '原料仓', 300, 2),
('A03', '奶油库位', 'A', '原料仓', 200, 3),
('A04', '通用原料库位', 'A', '原料仓', 400, 4),
('B01', '生产线1', 'B', '生产车间', 0, 1),
('B02', '生产线2', 'B', '生产车间', 0, 2),
('C01', '饼干库位', 'C', '成品仓', 1000, 1),
('C02', '面包库位', 'C', '成品仓', 800, 2),
('C03', '蛋糕库位', 'C', '成品仓', 600, 3),
('C04', '牛奶库位', 'C', '成品仓', 500, 4),
('C05', '饮料库位', 'C', '成品仓', 500, 5),
('D01', '发货暂存区', 'D', '发货区', 2000, 1);

-- 供应商
INSERT INTO `supplier` (`code`, `name`, `contact`, `phone`, `address`) VALUES
('SUP001', 'A食品有限公司', '张三', '13812345678', '北京市朝阳区'),
('SUP002', 'B原料供应公司', '李四', '13987654321', '上海市浦东新区'),
('SUP003', 'C乳制品集团', '王五', '13611112222', '内蒙古呼和浩特');

-- 原料
INSERT INTO `material` (`code`, `name`, `spec`, `unit`, `alert_threshold`) VALUES
('RM001', '面粉', '25kg/袋', '袋', 20),
('RM002', '白糖', '50kg/袋', '袋', 15),
('RM003', '奶油', '10kg/桶', '桶', 10),
('RM004', '鸡蛋', '30个/箱', '箱', 30),
('RM005', '牛奶原料', '1L/盒', '盒', 50);

-- 产品
INSERT INTO `product` (`code`, `name`, `spec`, `unit`, `shelf_life_days`, `alert_threshold`) VALUES
('PD001', '饼干', '500g/盒', '盒', 180, 100),
('PD002', '面包', '400g/袋', '袋', 7, 80),
('PD003', '蛋糕', '6寸/个', '个', 3, 50),
('PD004', '牛奶', '250ml/箱', '箱', 30, 50),
('PD005', '饮料', '500ml/瓶', '瓶', 365, 200);

-- 原料库存
INSERT INTO `material_stock` (`material_id`, `location_id`, `quantity`, `last_inbound_time`) VALUES
(1, 1, 120, NOW()),
(2, 2, 80, NOW()),
(3, 3, 8, NOW()),
(4, 4, 45, NOW()),
(5, 4, 200, NOW());

-- 成品库存
INSERT INTO `product_stock` (`product_id`, `location_id`, `quantity`, `production_date`, `expiry_date`, `delivery_date`, `last_inbound_time`) VALUES
(1, 7, 500, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 180 DAY), CURDATE(), NOW()),
(2, 8, 120, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 7 DAY), DATE_ADD(CURDATE(), INTERVAL 1 DAY), NOW()),
(3, 9, 80, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 3 DAY), DATE_ADD(CURDATE(), INTERVAL 7 DAY), NOW()),
(4, 10, 300, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 30 DAY), CURDATE(), NOW()),
(5, 11, 600, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 365 DAY), DATE_ADD(CURDATE(), INTERVAL 3 DAY), NOW());

-- 客户
INSERT INTO `customer` (`code`, `name`, `contact`, `phone`, `address`) VALUES
('CUS001', '华联超市', '陈经理', '13700001111', '广州市天河区'),
('CUS002', '沃尔玛', '林经理', '13700002222', '深圳市南山区'),
('CUS003', '家乐福', '黄经理', '13700003333', '成都市武侯区');

-- 库存预警(奶油不足示例)
INSERT INTO `stock_alert` (`type`, `ref_id`, `ref_name`, `current_qty`, `threshold`, `message`) VALUES
('MATERIAL', 3, '奶油', 8, 10, '⚠ 奶油库存不足，当前8桶，预警阈值10桶');

SET FOREIGN_KEY_CHECKS = 1;
