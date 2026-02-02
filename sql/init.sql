-- ----------------------------
-- Table structure for t_depart
-- ----------------------------
DROP TABLE IF EXISTS `t_depart`;
CREATE TABLE `t_depart`  (
                             `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                             `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门名称',
                             `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门编码',
                             `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门类型',
                             `create_user_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建用户id',
                             `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                             `update_user_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新用户id',
                             `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                             `version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '版本',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_depart
-- ----------------------------
INSERT INTO `t_depart` VALUES ('1', '财务', 'CW', '1', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_depart` VALUES ('2', '人事', 'RS', '1', NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for t_order_items
-- ----------------------------
DROP TABLE IF EXISTS `t_order_items`;
CREATE TABLE `t_order_items`  (
                                  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键ID（UUID）',
                                  `order_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '关联 orders.id',
                                  `product_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品ID',
                                  `sku_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'SKU ID（具体规格）',
                                  `product_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品名称（冗余）',
                                  `sku_desc` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'SKU描述，如“红色 / XL”',
                                  `price` decimal(10, 2) NOT NULL COMMENT '下单时单价',
                                  `quantity` int(11) NOT NULL DEFAULT 1 COMMENT '购买数量',
                                  `total_price` decimal(12, 2) NOT NULL COMMENT '小计 = price × quantity',
                                  `create_user_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建用户ID',
                                  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `update_user_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新用户ID',
                                  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                  `version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '版本号',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  INDEX `idx_order_id`(`order_id`) USING BTREE,
                                  INDEX `idx_product_id`(`product_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '订单明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_order_items
-- ----------------------------
INSERT INTO `t_order_items` VALUES ('2018228527631044608', '2018228527375192064', 'prod-001', 'sku-red-xl', '纯棉T恤', '红色 / XL', 149.99, 1, 149.99, '测试用户Id', '2026-02-02 15:42:33', '测试用户Id', '2026-02-02 15:42:33', '0');
INSERT INTO `t_order_items` VALUES ('2018228527635238912', '2018228527375192064', 'prod-002', 'sku-blue-m', '牛仔外套', '蓝色 / M', 299.98, 1, 299.98, '测试用户Id', '2026-02-02 15:42:33', '测试用户Id', '2026-02-02 15:42:33', '0');

-- ----------------------------
-- Table structure for t_orders
-- ----------------------------
DROP TABLE IF EXISTS `t_orders`;
CREATE TABLE `t_orders`  (
                             `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键ID（UUID）',
                             `order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订单编号，业务唯一，如 ORD202601290001',
                             `user_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '下单用户ID（业务用户）',
                             `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '订单状态：0-待支付，1-已支付，2-已发货，3-已完成，4-已取消，5-退款中',
                             `total_amount` decimal(12, 2) NOT NULL DEFAULT 0.00 COMMENT '订单总金额',
                             `actual_amount` decimal(12, 2) NOT NULL DEFAULT 0.00 COMMENT '实付金额',
                             `currency` char(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'CNY' COMMENT '货币代码',
                             `payment_method` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '支付方式',
                             `payment_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
                             `shipping_address_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '收货地址ID',
                             `consignee_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '收货人姓名（冗余）',
                             `consignee_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '收货人电话',
                             `shipping_fee` decimal(8, 2) NOT NULL DEFAULT 0.00 COMMENT '运费',
                             `discount_amount` decimal(8, 2) NOT NULL DEFAULT 0.00 COMMENT '优惠金额',
                             `coupon_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '使用的优惠券ID',
                             `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户备注',
                             `create_user_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建用户ID（操作人）',
                             `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `update_user_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新用户ID',
                             `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                             `version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '版本号',
                             PRIMARY KEY (`id`) USING BTREE,
                             UNIQUE INDEX `uk_order_no`(`order_no`) USING BTREE,
                             INDEX `idx_user_id`(`user_id`) USING BTREE,
                             INDEX `idx_status`(`status`) USING BTREE,
                             INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '订单主表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_orders
-- ----------------------------
INSERT INTO `t_orders` VALUES ('2018228527375192064', 'ORD1770018152408', 'user-123', 0, 449.97, 449.97, 'CNY', NULL, NULL, NULL, '张三', '13800138000', 0.00, 0.00, NULL, NULL, '测试用户Id', '2026-02-02 15:42:32', '测试用户Id', '2026-02-02 15:42:32', '0');

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
                           `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                           `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名称',
                           `age` int(11) NULL DEFAULT NULL COMMENT '年龄',
                           `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
                           `depart_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门id',
                           `create_user_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建用户id',
                           `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                           `update_user_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新用户id',
                           `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                           `version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                           PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('2013177515862003712', '哈哈2026-01-19 17:11:37', NULL, '哈哈2026-01-19 17:11:37@qq.com', NULL, '测试用户Id', '2026-01-19 17:11:37', '测试用户Id', '2026-01-19 17:11:37', '0');
INSERT INTO `t_user` VALUES ('2014654625591988224', 'aaa', 18, '哈哈2026-01-23 19:01:07@qq.com', '2', '测试用户Id', '2026-01-23 19:01:08', '测试用户Id', '2026-01-23 19:01:08', '0');
INSERT INTO `t_user` VALUES ('2014654733473681408', 'aaa', 18, '哈哈2026-01-23 19:01:28@qq.com', '1', '测试用户Id', '2026-01-23 19:01:34', '测试用户Id', '2026-01-23 19:01:34', '0');
INSERT INTO `t_user` VALUES ('2016084083460935680', '王五', NULL, 'wangwu@test.com', NULL, '测试用户Id', '2026-01-27 17:41:17', '测试用户Id', '2026-01-27 17:41:17', '0');
INSERT INTO `t_user` VALUES ('2016084084190744576', '赵六', NULL, 'zhaoliu@test.com', NULL, '测试用户Id', '2026-01-27 17:41:17', '测试用户Id', '2026-01-27 17:41:17', '0');

SET FOREIGN_KEY_CHECKS = 1;