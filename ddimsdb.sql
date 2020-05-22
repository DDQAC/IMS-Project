-- MySQL dump 10.13  Distrib 8.0.19, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: ddims
-- ------------------------------------------------------
-- Server version	8.0.20

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customers` (
  `CID` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(32) NOT NULL,
  `last_name` varchar(32) NOT NULL,
  `address_line_1` varchar(128) NOT NULL,
  `address_line_2` varchar(32) DEFAULT NULL,
  `city` varchar(32) NOT NULL,
  `postcode` char(8) NOT NULL,
  `email` varchar(320) NOT NULL,
  `loyalty` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`CID`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES (2,'Anne','Other','Somewhere nice',NULL,'Southampton','SO17 3BY','AnnieO@emailprovider.net',0),(3,'Testy','McTestface','13 Test Avenue','','Testville','TE12 3ST','TestyT@testmail.tst',0),(4,'Barry','Buble','The Barrows','Mulder Lane','Yeovil','BR54 3RF','BarryBuble@notMichael.com',0),(6,'David','Davenport','The Penthouse','Dave\'s flats','Southampton','SO15 0AA','DDavenport@academytrainee.com',0),(7,'Newname','Newsurname','12 New Road','','Newtown','NE12 3WX','newemail@emailclient.com',0);
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `items`
--

DROP TABLE IF EXISTS `items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `items` (
  `IID` int NOT NULL AUTO_INCREMENT,
  `FK_PID` int DEFAULT NULL,
  `name` varchar(64) NOT NULL,
  `price` decimal(5,2) NOT NULL,
  `stock` int NOT NULL DEFAULT '1',
  PRIMARY KEY (`IID`),
  KEY `FK_PID` (`FK_PID`),
  CONSTRAINT `items_ibfk_1` FOREIGN KEY (`FK_PID`) REFERENCES `plants` (`PID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `items`
--

LOCK TABLES `items` WRITE;
/*!40000 ALTER TABLE `items` DISABLE KEYS */;
INSERT INTO `items` VALUES (1,1,'Jade Plant',6.99,3),(3,NULL,'Trowel',4.99,4),(4,NULL,'gloves',3.99,1);
/*!40000 ALTER TABLE `items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `order_sum`
--

DROP TABLE IF EXISTS `order_sum`;
/*!50001 DROP VIEW IF EXISTS `order_sum`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `order_sum` AS SELECT 
 1 AS `order_ID`,
 1 AS `first_name`,
 1 AS `last_name`,
 1 AS `placed`,
 1 AS `order_total`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `OID` int NOT NULL AUTO_INCREMENT,
  `FK_CID` int NOT NULL,
  `placed` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`OID`),
  KEY `FK_CID` (`FK_CID`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`FK_CID`) REFERENCES `customers` (`CID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,6,'2020-05-12 11:06:19'),(2,2,'2020-05-19 10:12:10'),(3,3,'2020-05-21 19:30:48'),(4,3,'2020-05-21 19:35:12'),(5,3,'2020-05-21 19:50:23');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders_items`
--

DROP TABLE IF EXISTS `orders_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders_items` (
  `FK_OID` int NOT NULL,
  `FK_IID` int NOT NULL,
  `item_quantity` int NOT NULL DEFAULT '1',
  KEY `FK_OID` (`FK_OID`),
  KEY `FK_IID` (`FK_IID`),
  CONSTRAINT `orders_items_ibfk_1` FOREIGN KEY (`FK_OID`) REFERENCES `orders` (`OID`),
  CONSTRAINT `orders_items_ibfk_2` FOREIGN KEY (`FK_IID`) REFERENCES `items` (`IID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders_items`
--

LOCK TABLES `orders_items` WRITE;
/*!40000 ALTER TABLE `orders_items` DISABLE KEYS */;
INSERT INTO `orders_items` VALUES (2,1,3),(1,1,2),(1,4,1),(1,3,1),(1,1,2),(1,4,1),(1,3,1),(3,3,1),(3,1,2),(4,4,1),(5,1,2);
/*!40000 ALTER TABLE `orders_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `plants`
--

DROP TABLE IF EXISTS `plants`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `plants` (
  `PID` int NOT NULL AUTO_INCREMENT,
  `common_name` varchar(64) NOT NULL,
  `genus` varchar(32) NOT NULL,
  `species` varchar(32) DEFAULT NULL,
  `variegation` varchar(32) DEFAULT NULL,
  `light_req` enum('Full sun','Bright, indirect light','Shade') NOT NULL DEFAULT 'Bright, indirect light',
  `water_req` enum('High','Medium','Low') NOT NULL DEFAULT 'Low',
  PRIMARY KEY (`PID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `plants`
--

LOCK TABLES `plants` WRITE;
/*!40000 ALTER TABLE `plants` DISABLE KEYS */;
INSERT INTO `plants` VALUES (1,'Jade Plant','Crassula','Ovata','null','Bright, indirect light','Low');
/*!40000 ALTER TABLE `plants` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `UID` int NOT NULL AUTO_INCREMENT,
  `username` varchar(32) NOT NULL,
  `password` varchar(32) NOT NULL,
  `FK_CID` int DEFAULT NULL,
  PRIMARY KEY (`UID`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `FK_CID` (`FK_CID`),
  UNIQUE KEY `FK_CID_2` (`FK_CID`),
  CONSTRAINT `users_ibfk_1` FOREIGN KEY (`FK_CID`) REFERENCES `customers` (`CID`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'root','I am Groot',NULL),(2,'DDQAC','I am not Groot',6),(3,'Test1','Testing123',3),(4,'Test2','Testing123',4),(8,'AnnieO','AnotherOne',2),(9,'NewTest','Testing123',7);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Final view structure for view `order_sum`
--

/*!50001 DROP VIEW IF EXISTS `order_sum`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `order_sum` (`order_ID`,`first_name`,`last_name`,`placed`,`order_total`) AS select `ordersum`.`OID` AS `OID`,`c`.`first_name` AS `first_name`,`c`.`last_name` AS `last_name`,`ordersum`.`placed` AS `placed`,`ordersum`.`order_total` AS `order_total` from (`customers` `c` join (select `o`.`OID` AS `OID`,`o`.`FK_CID` AS `FK_CID`,`o`.`placed` AS `placed`,sum(`oijoin`.`total`) AS `order_total` from (`orders` `o` join (select `oi`.`FK_OID` AS `FK_OID`,`oi`.`FK_IID` AS `FK_IID`,(`oi`.`item_quantity` * `i`.`price`) AS `total` from (`orders_items` `oi` join `items` `i` on((`oi`.`FK_IID` = `i`.`IID`)))) `oijoin` on((`o`.`OID` = `oijoin`.`FK_OID`))) group by `oijoin`.`FK_OID`) `ordersum` on((`c`.`CID` = `ordersum`.`FK_CID`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-05-22  8:56:58
