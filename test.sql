/*
Navicat MySQL Data Transfer

Source Server         : Connection one
Source Server Version : 80016
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 80016
File Encoding         : 65001

Date: 2020-11-03 20:27:30
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for broadcast
-- ----------------------------
DROP TABLE IF EXISTS `broadcast`;
CREATE TABLE `broadcast` (
  `bc_id` int(11) NOT NULL,
  `class_id` int(11) NOT NULL,
  `message` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `publish_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for create_class
-- ----------------------------
DROP TABLE IF EXISTS `create_class`;
CREATE TABLE `create_class` (
  `uid` int(11) NOT NULL,
  `class_id` int(11) NOT NULL,
  `class_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for join_class
-- ----------------------------
DROP TABLE IF EXISTS `join_class`;
CREATE TABLE `join_class` (
  `uid` int(11) NOT NULL,
  `class_id` int(11) NOT NULL,
  `class_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for problemlist
-- ----------------------------
DROP TABLE IF EXISTS `problemlist`;
CREATE TABLE `problemlist` (
  `test_id` int(11) NOT NULL,
  `content` varchar(10000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `a` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `b` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `c` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `d` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `ans` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `publish_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `class_id` int(11) NOT NULL,
  `group_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for userinfo
-- ----------------------------
DROP TABLE IF EXISTS `userinfo`;
CREATE TABLE `userinfo` (
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `mailbox` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `school` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `school_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `uid` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_join_test
-- ----------------------------
DROP TABLE IF EXISTS `user_join_test`;
CREATE TABLE `user_join_test` (
  `uid` int(11) NOT NULL,
  `class_id` int(11) NOT NULL,
  `test_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `test_ans` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_join_vote
-- ----------------------------
DROP TABLE IF EXISTS `user_join_vote`;
CREATE TABLE `user_join_vote` (
  `vote_id` int(11) NOT NULL,
  `class_id` int(11) NOT NULL,
  `uid` int(11) NOT NULL,
  `option_selected` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for voteinfo
-- ----------------------------
DROP TABLE IF EXISTS `voteinfo`;
CREATE TABLE `voteinfo` (
  `class_id` int(11) NOT NULL,
  `vote_id` int(11) NOT NULL,
  `theme` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `option` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `option_num` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `publish_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `option_result` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
