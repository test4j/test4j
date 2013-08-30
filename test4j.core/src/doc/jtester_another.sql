--use db jtester_another;

SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE `another` (
  `id` int(11) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `field3` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `field3` (`field3`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `char_encoding_test` (
  `message` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `dbmaintain_scripts` (
  `file_name` varchar(150) DEFAULT NULL,
  `version` varchar(25) DEFAULT NULL,
  `file_last_modified_at` bigint(20) DEFAULT NULL,
  `checksum` varchar(50) DEFAULT NULL,
  `executed_at` varchar(20) DEFAULT NULL,
  `succeeded` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `demo_big_int_id` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'pk',
  `is_delete` char(1) NOT NULL DEFAULT 'N' COMMENT '逻辑删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `foreign_test` (
  `id` int(11) NOT NULL,
  `f_id` int(11) DEFAULT NULL,
  `not_null` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `f_id` (`f_id`),
  CONSTRAINT `foreign_test_fk` FOREIGN KEY (`f_id`) REFERENCES `tdd_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `jtester_address` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `modifior` varchar(255) DEFAULT NULL,
  `is_deleted` bit(1) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `postcode` varchar(255) DEFAULT NULL,
  `province` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4313875E892CE089` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `jtester_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `modifior` varchar(255) DEFAULT NULL,
  `is_deleted` bit(1) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tdd_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(20) DEFAULT NULL,
  `last_name` varchar(20) DEFAULT NULL,
  `post_code` char(6) DEFAULT NULL,
  `sarary` double(15,3) DEFAULT NULL,
  `address_id` int(11) DEFAULT NULL,
  `my_date` datetime DEFAULT NULL,
  `big_int` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;