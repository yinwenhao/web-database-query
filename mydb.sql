/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50624
Source Host           : localhost:3306
Source Database       : mydb

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2015-07-01 14:20:07
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_config
-- ----------------------------
DROP TABLE IF EXISTS `t_config`;
CREATE TABLE `t_config` (
  `key` varchar(30) NOT NULL,
  `value` varchar(255) DEFAULT '',
  PRIMARY KEY (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_database
-- ----------------------------
DROP TABLE IF EXISTS `t_database`;
CREATE TABLE `t_database` (
  `id` int(11) NOT NULL,
  `url` varchar(255) NOT NULL COMMENT 'jdbc.url',
  `username` varchar(20) NOT NULL,
  `password` varchar(32) NOT NULL,
  `driver` varchar(255) NOT NULL DEFAULT 'com.mysql.jdbc.Driver' COMMENT 'jdbc的引擎类',
  `name` varchar(20) DEFAULT '' COMMENT '数据库显示名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_secret_column
-- ----------------------------
DROP TABLE IF EXISTS `t_secret_column`;
CREATE TABLE `t_secret_column` (
  `database_id` int(11) NOT NULL COMMENT '数据库id',
  `secret_columns` varchar(500) DEFAULT '' COMMENT '例如：t_customer:mobile,password;test:t1,t2,ccc',
  PRIMARY KEY (`database_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(20) NOT NULL,
  `password` varchar(32) NOT NULL,
  `permission_level` tinyint(4) DEFAULT '0' COMMENT '权限等级',
  `permission` varchar(255) DEFAULT '' COMMENT '数据库id，英文逗号隔开',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_account` (`account`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_query_log`;
CREATE TABLE `t_query_log` (
`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
`database_id` BIGINT(20) NOT NULL COMMENT '执行查询的目标库',
   `user_name` VARCHAR(20)  COMMENT '执行查询的用户名',
   `query_statement` VARCHAR(2000)  COMMENT '查询语句',
   `query_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '执行查询的时间',
   `query_cost` BIGINT(20) COMMENT '查询耗时,单位ms',
   `state` BIGINT(5) COMMENT '执行状态',
   `error_message` VARCHAR(200) DEFAULT NULL COMMENT '出错信息',
   PRIMARY KEY (`id`)
   )ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
