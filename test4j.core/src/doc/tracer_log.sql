CREATE TABLE `tracer_log` (
  `test_user` varchar(20) NOT NULL,
  `test_clazz` varchar(100) NOT NULL,
  `test_method` varchar(100) NOT NULL,
  `test_log` longtext NOT NULL,
  `test_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;