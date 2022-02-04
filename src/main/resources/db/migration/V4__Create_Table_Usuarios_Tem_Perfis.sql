CREATE TABLE `usuarios_tem_perfis` (
  `usuario_id` bigint NOT NULL,
  `perfil_id` bigint NOT NULL,
  KEY `FKewye59sxbuklud72lsswd1mn1` (`perfil_id`),
  KEY `FK3nbkf47whuccb4vso9v3of524` (`usuario_id`),
  CONSTRAINT `FK3nbkf47whuccb4vso9v3of524` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`),
  CONSTRAINT `FKewye59sxbuklud72lsswd1mn1` FOREIGN KEY (`perfil_id`) REFERENCES `perfis` (`id`)
)