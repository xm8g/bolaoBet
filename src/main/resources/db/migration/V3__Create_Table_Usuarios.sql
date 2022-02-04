CREATE TABLE IF NOT EXISTS `usuario` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `ativo` tinyint(1) NOT NULL,
  `codigo_verificador` varchar(6) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `senha` varchar(255) NOT NULL,
  `avatar_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_5171l57faosmj8myawaucatdw` (`email`),
  UNIQUE KEY `UKeafgkh3ukqlfc62wbonlq90gt` (`avatar_id`),
  CONSTRAINT `FKg1aexgfpi3rvs1v5usk88nb19` FOREIGN KEY (`avatar_id`) REFERENCES `avatar` (`id`)
)