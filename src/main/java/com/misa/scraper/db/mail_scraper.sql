CREATE TABLE `mail` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `recipient` varchar(45) DEFAULT NULL,
  `subject` varchar(450) DEFAULT NULL,
  `content` varchar(45) DEFAULT NULL,
  `sent_date` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idmail_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci