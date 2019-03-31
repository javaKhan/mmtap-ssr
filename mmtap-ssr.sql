/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50721
Source Host           : 127.0.0.1:3306
Source Database       : mmtap-ssr

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2019-01-18 18:00:55
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` datetime DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `address_ip` varchar(255) DEFAULT NULL,
  `browser_name` varchar(255) DEFAULT NULL,
  `controller` varchar(255) DEFAULT NULL,
  `driver_name` varchar(255) DEFAULT NULL,
  `exception_info` varchar(255) DEFAULT NULL,
  `execute_use_millisecond` bigint(20) DEFAULT NULL,
  `method_name` varchar(255) DEFAULT NULL,
  `req_method` varchar(255) DEFAULT NULL,
  `req_uri` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` datetime DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `authority` varchar(255) DEFAULT NULL,
  `depth` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `skin` varchar(255) DEFAULT NULL,
  `sort` bigint(20) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2jrf4gb0gjqi8882gxytpxnhe` (`parent_id`)
) ENGINE=MyISAM AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('1', '2019-01-17 02:22:28', '2019-01-17 02:22:28', null, 'ROOT', null, null, 'root', null, '0', null, null);
INSERT INTO `sys_menu` VALUES ('2', '2019-01-17 02:22:28', '2019-01-18 06:17:32', null, 'SYSTEM', null, 'zmdi zmdi-shield-security', '权限管理系统', 'skin-blue', '2', '', '1');
INSERT INTO `sys_menu` VALUES ('3', '2019-01-17 02:25:17', '2019-01-18 06:26:05', null, 'SYSTEM', null, 'zmdi zmdi-wikipedia', '内容管理系统', 'skin-dark-blue', '24', '', '1');
INSERT INTO `sys_menu` VALUES ('4', '2019-01-17 02:25:50', '2019-01-18 06:26:05', null, 'SYSTEM', null, 'zmdi zmdi-paypal-alt', '支付管理系统', 'skin-pink', '34', '', '1');
INSERT INTO `sys_menu` VALUES ('5', '2019-01-17 02:27:10', '2019-01-18 06:26:05', null, 'SYSTEM', null, 'zmdi zmdi-account', '用户管理系统', 'skin-purple', '38', '', '1');
INSERT INTO `sys_menu` VALUES ('6', '2019-01-17 02:32:47', '2019-01-18 06:26:04', null, 'DIRECTORY', null, 'zmdi zmdi-lock-outline', '权限管理', null, '5', '', '2');
INSERT INTO `sys_menu` VALUES ('7', '2019-01-17 02:34:28', '2019-01-18 06:26:04', null, 'MENU', null, 'zmdi zmdi-accounts', '用户管理', null, '6', 'page/sys/user/table.html', '6');
INSERT INTO `sys_menu` VALUES ('8', '2019-01-17 02:35:17', '2019-01-18 06:26:04', null, 'MENU', null, 'zmdi zmdi-menu', '菜单管理', 'skin-green', '8', 'page/sys/menu/menu.html', '6');
INSERT INTO `sys_menu` VALUES ('9', '2019-01-17 02:35:42', '2019-01-18 06:26:05', null, 'MENU', null, 'zmdi zmdi-flower-alt', '角色管理', 'skin-green', '10', 'page/sys/role/table.html', '6');
INSERT INTO `sys_menu` VALUES ('10', '2019-01-17 09:39:04', '2019-01-18 06:26:05', null, 'DIRECTORY', null, 'zmdi zmdi-assignment', '监控管理', 'skin-green', '13', '', '2');
INSERT INTO `sys_menu` VALUES ('11', '2019-01-17 09:39:55', '2019-01-18 06:26:05', null, 'MENU', null, 'zmdi zmdi-assignment', '操作日志', 'skin-green', '14', 'page/sys/log/table.html', '10');
INSERT INTO `sys_menu` VALUES ('12', '2019-01-17 09:59:17', '2019-01-18 06:26:05', null, 'DIRECTORY', null, 'zmdi zmdi-memory', '其他', 'skin-green', '17', '', '2');
INSERT INTO `sys_menu` VALUES ('13', '2019-01-17 09:59:34', '2019-01-18 06:26:05', null, 'MENU', null, 'zmdi zmdi-shape', '图标', 'skin-green', '18', 'http://zavoloklom.github.io/material-design-iconic-font/icons.html', '12');
INSERT INTO `sys_menu` VALUES ('14', '2019-01-17 10:10:28', '2019-01-18 06:26:05', null, 'MENU', null, 'zmdi zmdi-search', 'bing', 'skin-green', '20', 'https://bing.com', '12');
INSERT INTO `sys_menu` VALUES ('15', '2019-01-18 06:11:53', '2019-01-18 06:26:05', null, 'DIRECTORY', null, '', '用户管理', null, '39', '', '5');
INSERT INTO `sys_menu` VALUES ('16', '2019-01-18 06:12:09', '2019-01-18 06:26:05', null, 'DIRECTORY', null, '', '支付管理', null, '35', '', '4');
INSERT INTO `sys_menu` VALUES ('17', '2019-01-18 06:12:20', '2019-01-18 06:26:05', null, 'DIRECTORY', null, '', '内容管理', null, '25', '', '3');
INSERT INTO `sys_menu` VALUES ('18', '2019-01-18 06:13:48', '2019-01-18 06:26:05', null, 'MENU', null, '', 'bing', 'skin-green', '26', 'http://bing.com', '17');
INSERT INTO `sys_menu` VALUES ('19', '2019-01-18 06:15:38', '2019-01-18 06:26:05', null, 'MENU', null, '', 'v2ex', null, '28', 'https://www.v2ex.com/', '17');
INSERT INTO `sys_menu` VALUES ('20', '2019-01-18 06:16:13', '2019-01-18 06:26:05', null, 'MENU', null, '', 'github', null, '30', 'https://github.com/', '17');
INSERT INTO `sys_menu` VALUES ('21', '2019-01-18 06:23:44', '2019-01-18 06:26:04', null, 'DIRECTORY', null, 'zmdi zmdi-home', '首页', null, '3', 'home', '2');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `authority` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', null, '2019-01-18 14:53:49', '2019-01-18 06:54:53', 'ROLE_admin', 'admin');

-- ----------------------------
-- Table structure for sys_role_menu_set
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu_set`;
CREATE TABLE `sys_role_menu_set` (
  `role_id` bigint(20) NOT NULL,
  `menu_set_id` bigint(20) NOT NULL,
  PRIMARY KEY (`role_id`,`menu_set_id`),
  KEY `FKawfxxpl126afgi5pha701u4ut` (`menu_set_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role_menu_set
-- ----------------------------
INSERT INTO `sys_role_menu_set` VALUES ('1', '1');
INSERT INTO `sys_role_menu_set` VALUES ('1', '2');
INSERT INTO `sys_role_menu_set` VALUES ('1', '3');
INSERT INTO `sys_role_menu_set` VALUES ('1', '4');
INSERT INTO `sys_role_menu_set` VALUES ('1', '5');
INSERT INTO `sys_role_menu_set` VALUES ('1', '6');
INSERT INTO `sys_role_menu_set` VALUES ('1', '7');
INSERT INTO `sys_role_menu_set` VALUES ('1', '8');
INSERT INTO `sys_role_menu_set` VALUES ('1', '9');
INSERT INTO `sys_role_menu_set` VALUES ('1', '10');
INSERT INTO `sys_role_menu_set` VALUES ('1', '11');
INSERT INTO `sys_role_menu_set` VALUES ('1', '12');
INSERT INTO `sys_role_menu_set` VALUES ('1', '13');
INSERT INTO `sys_role_menu_set` VALUES ('1', '14');
INSERT INTO `sys_role_menu_set` VALUES ('1', '15');
INSERT INTO `sys_role_menu_set` VALUES ('1', '16');
INSERT INTO `sys_role_menu_set` VALUES ('1', '17');
INSERT INTO `sys_role_menu_set` VALUES ('1', '18');
INSERT INTO `sys_role_menu_set` VALUES ('1', '19');
INSERT INTO `sys_role_menu_set` VALUES ('1', '20');
INSERT INTO `sys_role_menu_set` VALUES ('1', '21');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` datetime DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `gender` int(11) DEFAULT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `status` int(11) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_51bvuyvihefoh4kp5syh2jpi4` (`username`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', '2019-01-18 06:47:05', '2019-01-18 09:59:42', 'admin', 'test@qq.com', '0', 'admin', '(188) 5419-2893', '{bcrypt}$2a$10$IwmSbGWXuXWN9BTYSRiIBO5ZqLtigXTlkc0eRXNn7RvJZUyz0mZLe', '0', 'admin');

-- ----------------------------
-- Table structure for sys_user_role_set
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role_set`;
CREATE TABLE `sys_user_role_set` (
  `user_id` bigint(20) NOT NULL,
  `role_set_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role_set_id`),
  KEY `FKra4oghl8c07kvu96555lu4og9` (`role_set_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user_role_set
-- ----------------------------
INSERT INTO `sys_user_role_set` VALUES ('1', '1');
