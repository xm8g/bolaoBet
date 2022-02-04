CREATE TABLE IF NOT EXISTS `perfis` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `descricao` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_46fwiur1v4jn08eg093a3bckv` (`descricao`)
)