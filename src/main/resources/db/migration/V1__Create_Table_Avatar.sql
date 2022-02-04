CREATE TABLE IF NOT EXISTS `avatar` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `data` longblob,
  `file_name` varchar(255) DEFAULT NULL,
  `file_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
)