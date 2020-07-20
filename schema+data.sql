-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.20 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             11.0.0.5919
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE=''NO_AUTO_VALUE_ON_ZERO'' */;


-- Dumping database structure for ebuydb
DROP DATABASE IF EXISTS `ebuydb`;
CREATE DATABASE IF NOT EXISTS `ebuydb` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION=''N'' */;
USE `ebuydb`;

-- Dumping structure for table ebuydb.account
CREATE TABLE IF NOT EXISTS `account` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(50) NOT NULL,
  `isadmin` smallint NOT NULL,
  `nickname` varchar(20) NOT NULL,
  `password` varchar(64) NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table ebuydb.account: ~7 rows (approximately)
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` (`user_id`, `email`, `isadmin`, `nickname`, `password`) VALUES
	(1, ''admin@ebuy.com'', 1, ''admin'', ''8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918''),
	(2, ''user@ebuy.com'', 0, ''user'', ''04f8996da763b7a969b1028ee3007569eaf3a635486ddab211d512c85b9df8fb''),
	(3, ''u1@sample.com'', 0, ''u1'', ''bb82030dbc2bcaba32a90bf2e207a84a856fc5f033b77c480836ab6f77f40f19''),
	(4, ''u2@sample.com'', 0, ''u2'', ''6ca202c88e549dff68c09bfafbfc60b2fac074debc1e6777e9ba4b6c703ed114''),
	(5, ''u3@sample.com'', 0, ''u3'', ''011e39efe22590f4a339ad19cd180f4d855e32feba602d1ec8e154780838c99c''),
	(6, ''u4@sample.com'', 0, ''u4'', ''e9c981a479986215bab0bf6c32efefa14852534b138c3509d8369edd510363da''),
	(7, ''u5@sample.com'', 0, ''u5'', ''5850a03e801ffb108da1160e3373979443004b9e670addf33000dca9045fa413'');
/*!40000 ALTER TABLE `account` ENABLE KEYS */;

-- Dumping structure for table ebuydb.category
CREATE TABLE IF NOT EXISTS `category` (
  `category_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table ebuydb.category: ~0 rows (approximately)
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` (`category_id`, `name`) VALUES
	(1, ''deportes''),
	(2, ''electronica''),
	(3, ''comida''),
	(4, ''videojuegos''),
	(5, ''hogar'');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;

-- Dumping structure for table ebuydb.subcategory
CREATE TABLE IF NOT EXISTS `subcategory` (
  `subcategory_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  `category_id` int NOT NULL,
  PRIMARY KEY (`subcategory_id`),
  KEY `FKe4hdbsmrx9bs9gpj1fh4mg0ku` (`category_id`),
  CONSTRAINT `FKe4hdbsmrx9bs9gpj1fh4mg0ku` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table ebuydb.subcategory: ~0 rows (approximately)
/*!40000 ALTER TABLE `subcategory` DISABLE KEYS */;
INSERT INTO `subcategory` (`subcategory_id`, `name`, `category_id`) VALUES
	(1, ''deportes'', 1),
	(2, ''aire-libre'', 1),
	(3, ''agua'', 1),
	(4, ''electronica'', 2),
	(5, ''ordenadores'', 2),
	(6, ''moviles'', 2),
	(7, ''televisiones'', 2),
	(8, ''bebida'', 3),
	(9, ''lacteos'', 3),
	(10, ''snacks'', 3),
	(11, ''italiana'', 3),
	(12, ''multiplataforma'', 4),
	(13, ''accesorios'', 4),
	(14, ''PC'', 4),
	(15, ''Nintendo'', 4),
	(16, ''Sony'', 4),
	(17, ''Microsoft'', 4),
	(18, ''hogar'', 5),
	(19, ''bebes'', 5),
	(20, ''electrodomesticos'', 5),
	(21, ''interior'', 1),
	(22, ''comida'', 3);
/*!40000 ALTER TABLE `subcategory` ENABLE KEYS */;

-- Dumping structure for table ebuydb.keyword
CREATE TABLE IF NOT EXISTS `keyword` (
  `keyword_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`keyword_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table ebuydb.keyword: ~0 rows (approximately)
/*!40000 ALTER TABLE `keyword` DISABLE KEYS */;
INSERT INTO `keyword` (`keyword_id`, `name`) VALUES
	(1, ''deporte''),
	(2, ''enCasa''),
	(3, ''lacteos''),
	(4, ''nuevos'');
/*!40000 ALTER TABLE `keyword` ENABLE KEYS */;

-- Dumping structure for table ebuydb.product
CREATE TABLE IF NOT EXISTS `product` (
  `product_id` int NOT NULL AUTO_INCREMENT,
  `creation_date` date NOT NULL,
  `creation_time` time NOT NULL,
  `description` varchar(1020) DEFAULT NULL,
  `photo_url` varchar(510) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `quantity` int NOT NULL,
  `title` varchar(50) NOT NULL,
  `vendor_id` int NOT NULL,
  `subcategory_id` int NOT NULL,
  PRIMARY KEY (`product_id`),
  KEY `FKkxy9ywup2eb51odkld6wp8rar` (`vendor_id`),
  KEY `FKku369nri8u3s17uom8or57trs` (`subcategory_id`),
  CONSTRAINT `FKku369nri8u3s17uom8or57trs` FOREIGN KEY (`subcategory_id`) REFERENCES `subcategory` (`subcategory_id`),
  CONSTRAINT `FKkxy9ywup2eb51odkld6wp8rar` FOREIGN KEY (`vendor_id`) REFERENCES `account` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table ebuydb.product: ~0 rows (approximately)
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` (`product_id`, `creation_date`, `creation_time`, `description`, `photo_url`, `price`, `quantity`, `title`, `vendor_id`, `subcategory_id`) VALUES
	(1, ''2020-03-24'', ''20:05:00'', ''JUEGAZO'', ''https://sgfm.elcorteingles.es/SGFM/dctm/MEDIA03/202001/13/00197580504765____4__640x640.jpg'', 60, 20, ''Animal Crossing'', 5, 15),
	(2, ''2020-03-21'', ''17:20:00'', ''bici muy bonita'', ''https://fabricbike.com/63-large_default/fixie-light.jpg'', 230, 6, ''bici'', 2, 2),
	(3, ''2020-03-21'', ''10:05:00'', ''yogur stracciatella sin lactosa'', ''https://sgfm.elcorteingles.es/SGFM/dctm/MEDIA03/201708/31/00118823001013____6__600x600.jpg'', 2, 10, ''yogur'', 2, 9),
	(4, ''2020-04-06'', ''15:10:00'', ''La Life Fitness Tomahawk IC1 aporta una increible experiencia con las bicis indoor a tu hogar.Con ingenieria provada de ICG, esta bici esta hecha para durar mas. Gracias a su marco elegante, la Life Fitness IC1 entra perfectamente en cualquier espacio. '', ''https://www.gymcompany.es/media/catalog/product/cache/1/thumbnail/9df78eab33525d08d6e5fb8d27136e95/i/c/ic1-02-1000x1000.jpg'', 795, 5, ''Life Fitness Tomahawk IC1 Bicicleta Indoor'', 2, 21),
	(5, ''2020-04-06'', ''15:05:00'', ''Salmorejo Mercadona'', ''https://s1.eestatic.com/2019/04/30/ciencia/nutricion/Tomate-Precios-Aceite_de_oliva-Ajo-Pimiento-OCU_Organizacion_de_Consumidores_y_Usuarios-Nutricion_394972210_121694460_1706x1280.jpg'', 2.5, 3, ''Salmorejo'', 2, 22),
	(6, ''2020-04-17'', ''09:45:00'', ''Pizza jamon y queso marca ristorante'', ''https://sgfm.elcorteingles.es/SGFM/dctm/MEDIA03/201810/23/00118950100968____2__600x600.jpg'', 3.49, 8, ''Pizza jamon y queso'', 2, 11),
	(7, ''2020-04-20'', ''22:47:50'', ''assad'', ''https://distribucionesplata.com/tienda/18564-thickbox_default/salsa-heinz-barbacoa-220-ml.jpg'', 12, 10, ''salsa barbacoa'', 2, 22);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;

-- Dumping structure for table ebuydb.product_keyword
CREATE TABLE IF NOT EXISTS `product_keyword` (
  `product_id` int NOT NULL,
  `keyword_id` int NOT NULL,
  PRIMARY KEY (`product_id`,`keyword_id`),
  KEY `FKndmhxrgtuhbk8csponj784iwi` (`keyword_id`),
  CONSTRAINT `FK7t104v2rcj4fxv6bf714x36f5` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`),
  CONSTRAINT `FKndmhxrgtuhbk8csponj784iwi` FOREIGN KEY (`keyword_id`) REFERENCES `keyword` (`keyword_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table ebuydb.product_keyword: ~0 rows (approximately)
/*!40000 ALTER TABLE `product_keyword` DISABLE KEYS */;
INSERT INTO `product_keyword` (`product_id`, `keyword_id`) VALUES
	(4, 1),
	(1, 2),
	(4, 2),
	(5, 2),
	(1, 4),
	(5, 4);
/*!40000 ALTER TABLE `product_keyword` ENABLE KEYS */;

-- Dumping structure for table ebuydb.purchased_product
CREATE TABLE IF NOT EXISTS `purchased_product` (
  `purchase_id` int NOT NULL AUTO_INCREMENT,
  `price` double NOT NULL,
  `purchase_date` date NOT NULL,
  `quantity` int NOT NULL,
  `buyer_id` int NOT NULL,
  `product_id` int NOT NULL,
  PRIMARY KEY (`purchase_id`),
  KEY `FK23a3o6piv58wtp2olkqv78rik` (`buyer_id`),
  KEY `FK110bv4ucb0b6cyi0k7vnq8dhc` (`product_id`),
  CONSTRAINT `FK110bv4ucb0b6cyi0k7vnq8dhc` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`),
  CONSTRAINT `FK23a3o6piv58wtp2olkqv78rik` FOREIGN KEY (`buyer_id`) REFERENCES `account` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table ebuydb.purchased_product: ~0 rows (approximately)
/*!40000 ALTER TABLE `purchased_product` DISABLE KEYS */;
INSERT INTO `purchased_product` (`purchase_id`, `price`, `purchase_date`, `quantity`, `buyer_id`, `product_id`) VALUES
	(1, 3.49, ''2020-04-21'', 3, 3, 6),
	(2, 795, ''2020-04-21'', 1, 3, 4),
	(3, 60, ''2020-04-21'', 3, 2, 1),
	(4, 12, ''2020-04-21'', 1, 4, 7),
	(5, 2, ''2020-04-21'', 4, 4, 3),
	(6, 2.5, ''2020-04-21'', 1, 4, 5),
	(7, 230, ''2020-04-21'', 2, 4, 2),
	(8, 795, ''2020-04-21'', 1, 6, 4),
	(9, 230, ''2020-04-21'', 1, 6, 2);
/*!40000 ALTER TABLE `purchased_product` ENABLE KEYS */;

-- Dumping structure for table ebuydb.review
CREATE TABLE IF NOT EXISTS `review` (
  `review_id` int NOT NULL AUTO_INCREMENT,
  `comment` varchar(510) DEFAULT NULL,
  `review_date` date NOT NULL,
  `stars` double NOT NULL,
  `user_id` int NOT NULL,
  `purchase_id` int NOT NULL,
  PRIMARY KEY (`review_id`),
  UNIQUE KEY `UK_qv55tbygybrtvuyut6ropvfpr` (`purchase_id`),
  KEY `FKipmoly19mx9xx4f6hoyqdpmys` (`user_id`),
  CONSTRAINT `FK76jc0lybfyrupvc75kanhnjyo` FOREIGN KEY (`purchase_id`) REFERENCES `purchased_product` (`purchase_id`),
  CONSTRAINT `FKipmoly19mx9xx4f6hoyqdpmys` FOREIGN KEY (`user_id`) REFERENCES `account` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table ebuydb.review: ~0 rows (approximately)
/*!40000 ALTER TABLE `review` DISABLE KEYS */;
INSERT INTO `review` (`review_id`, `comment`, `review_date`, `stars`, `user_id`, `purchase_id`) VALUES
	(1, ''Muy rica'', ''2020-04-21'', 5, 3, 1),
	(2, ''Demasiado cara'', ''2020-04-21'', 3.5, 3, 2),
	(3, ''GOTY'', ''2020-04-21'', 5, 2, 3),
	(4, ''Prefiero la de Hacendado'', ''2020-04-21'', 3.5, 4, 4),
	(5, ''Perfecto para intolerantes :)'', ''2020-04-21'', 5, 4, 5),
	(6, ''En El Corte Inglés está menos bueno.'', ''2020-04-21'', 4.5, 4, 6),
	(7, ''No puedo usarla en el confinamiento'', ''2020-04-21'', 1, 4, 7),
	(8, ''Muy buena'', ''2020-04-21'', 5, 6, 8),
	(9, ''Color muy feo.'', ''2020-04-21'', 3, 6, 9);
/*!40000 ALTER TABLE `review` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '''') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
