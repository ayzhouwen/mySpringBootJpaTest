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