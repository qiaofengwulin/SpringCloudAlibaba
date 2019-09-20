/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50535
 Source Host           : localhost:3306
 Source Schema         : pay

 Target Server Type    : MySQL
 Target Server Version : 50535
 File Encoding         : 65001

 Date: 20/09/2019 15:43:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for dict
-- ----------------------------
DROP TABLE IF EXISTS `dict`;
CREATE TABLE `dict`  (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `table_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '所属表名',
  `content` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '备注',
  `key` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '键',
  `value` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for hos
-- ----------------------------
DROP TABLE IF EXISTS `hos`;
CREATE TABLE `hos`  (
  `id` int(4) NOT NULL AUTO_INCREMENT COMMENT '医院ID',
  `hos_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '医院名称',
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `hos_name`(`hos_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for qh_config
-- ----------------------------
DROP TABLE IF EXISTS `qh_config`;
CREATE TABLE `qh_config`  (
  `id` int(5) NOT NULL COMMENT '主键',
  `hos_id` int(64) NOT NULL COMMENT '医院ID',
  `hos_key` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '配置名称',
  `hos_value` varchar(4096) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '值',
  `content` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `mch_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `hos`(`hos_id`, `hos_key`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for qh_paylog
-- ----------------------------
DROP TABLE IF EXISTS `qh_paylog`;
CREATE TABLE `qh_paylog`  (
  `trade_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `pay_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `fee` decimal(32, 0) DEFAULT NULL,
  `pay_number` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `out_trade_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `out_trade_refund_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `mch_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `create_time` varchar(0) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '',
  `trade_time` date DEFAULT NULL,
  PRIMARY KEY (`trade_no`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
