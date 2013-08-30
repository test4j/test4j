--use db presentationtdd

SET FOREIGN_KEY_CHECKS=0;

--CREATE DATABASE `presentationtdd`
--    CHARACTER SET 'utf8'
--    COLLATE 'utf8_general_ci';
--USE `presentationtdd`;

#
# Structure for the `another` table : 
#

CREATE TABLE `another` (
  `id` INTEGER(11) NOT NULL,
  `name` VARCHAR(50) COLLATE utf8_general_ci DEFAULT NULL,
  `age` INTEGER(11) DEFAULT NULL,
  `field3` INTEGER(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `field3` (`field3`)
)ENGINE=InnoDB
CHARACTER SET 'utf8' COLLATE 'utf8_general_ci';

#
# Structure for the `char_encoding_test` table : 
#

CREATE TABLE `char_encoding_test` (
  `message` VARCHAR(20) COLLATE utf8_general_ci DEFAULT NULL
)ENGINE=InnoDB
CHARACTER SET 'utf8' COLLATE 'utf8_general_ci';


#
# Structure for the `jtester_address` table : 
#

CREATE TABLE `jtester_address` (
  `id` INTEGER(11) NOT NULL AUTO_INCREMENT,
  `create_date` DATETIME DEFAULT NULL,
  `creator` VARCHAR(255) COLLATE utf8_general_ci DEFAULT NULL,
  `modified_date` DATETIME DEFAULT NULL,
  `modifior` VARCHAR(255) COLLATE utf8_general_ci DEFAULT NULL,
  `is_deleted` BIT(1) DEFAULT NULL,
  `address` VARCHAR(255) COLLATE utf8_general_ci DEFAULT NULL,
  `country` VARCHAR(255) COLLATE utf8_general_ci DEFAULT NULL,
  `city` VARCHAR(255) COLLATE utf8_general_ci DEFAULT NULL,
  `postcode` VARCHAR(255) COLLATE utf8_general_ci DEFAULT NULL,
  `province` VARCHAR(255) COLLATE utf8_general_ci DEFAULT NULL,
  `user_id` INTEGER(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4313875E892CE089` (`user_id`)
)ENGINE=InnoDB
AUTO_INCREMENT=2 CHARACTER SET 'utf8' COLLATE 'utf8_general_ci';

#
# Structure for the `jtester_user` table : 
#

CREATE TABLE `jtester_user` (
  `id` INTEGER(11) NOT NULL AUTO_INCREMENT,
  `create_date` DATETIME DEFAULT NULL,
  `creator` VARCHAR(255) COLLATE utf8_general_ci DEFAULT NULL,
  `modified_date` DATETIME DEFAULT NULL,
  `modifior` VARCHAR(255) COLLATE utf8_general_ci DEFAULT NULL,
  `is_deleted` BIT(1) DEFAULT NULL,
  `name` VARCHAR(255) COLLATE utf8_general_ci DEFAULT NULL,
  `email` VARCHAR(255) COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
)ENGINE=InnoDB
AUTO_INCREMENT=44 CHARACTER SET 'utf8' COLLATE 'utf8_general_ci';

#
# Structure for the `tdd_user` table : 
#

CREATE TABLE `tdd_user` (
  `id` INTEGER(11) NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(20) COLLATE utf8_general_ci DEFAULT NULL,
  `last_name` VARCHAR(20) COLLATE utf8_general_ci DEFAULT NULL,
  `post_code` CHAR(6) COLLATE utf8_general_ci DEFAULT NULL,
  `sarary` DOUBLE(15,3) DEFAULT NULL,
  `address_id` INTEGER(11) DEFAULT NULL,
  `my_date` DATETIME DEFAULT NULL,
  `big_int` BIGINT(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
)ENGINE=InnoDB
AUTO_INCREMENT=1228 CHARACTER SET 'utf8' COLLATE 'utf8_general_ci';

CREATE TABLE `all_types` (
  `TINYINT` tinyint(4) DEFAULT NULL,
  `SMALLINT` smallint(6) DEFAULT NULL,
  `MEDIUMINT` mediumint(9) DEFAULT NULL,
  `INTEGER` int(11) NOT NULL AUTO_INCREMENT,
  `BIGINT` bigint(20) DEFAULT NULL,
  `FLOAT` float(9,3) DEFAULT NULL,
  `DOUBLE` double(15,3) DEFAULT NULL,
  `DECIMAL` decimal(11,0) DEFAULT NULL,
  `DATE` date DEFAULT NULL,
  `DATETIME` datetime DEFAULT NULL,
  `TIME` time DEFAULT NULL,
  `YEAR` year(4) DEFAULT NULL,
  `CHAR` char(20) DEFAULT NULL,
  `VARCHAR` varchar(20) DEFAULT NULL,
  `TINYBLOB` tinyblob,
  `BLOB` blob,
  `MEDIUMBLOB` mediumblob,
  `LONGBLOB` longblob,
  `TINYTEXT` tinytext,
  `TEXT` text,
  `MEDIUMTEXT` mediumtext,
  `LONGTEXT` longtext,
  `SET` set('1','2','3','4') DEFAULT NULL,
  `BINARY` binary(255) DEFAULT NULL,
  `VARBINARY` varbinary(1000) DEFAULT NULL,
  `BIT` bit(1) DEFAULT NULL,
  `BOOLEAN` tinyint(1) DEFAULT NULL,
  `ENUM` enum('1','2') DEFAULT NULL,
  PRIMARY KEY (`INTEGER`),
  UNIQUE KEY `INTEGER` (`INTEGER`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

CREATE TABLE `all_type` (
  `id` INTEGER(11) NOT NULL AUTO_INCREMENT,
  `my_tinyint_unsigned` TINYINT(4) UNSIGNED DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
)ENGINE=InnoDB
AUTO_INCREMENT=2 CHARACTER SET 'utf8' COLLATE 'utf8_general_ci';

CREATE TABLE `demo_big_int_id` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'pk',
  `is_delete` CHAR(1) COLLATE utf8_general_ci NOT NULL DEFAULT 'N' COMMENT '逻辑删除标志',
  PRIMARY KEY (`id`)
)ENGINE=InnoDB
AUTO_INCREMENT=123457 CHARACTER SET 'utf8' COLLATE 'utf8_general_ci';

