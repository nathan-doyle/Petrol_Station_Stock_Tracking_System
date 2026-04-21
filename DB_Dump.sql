CREATE DATABASE  IF NOT EXISTS `petrol_station_schema` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `petrol_station_schema`;
-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: petrol_station_schema
-- ------------------------------------------------------
-- Server version	8.0.44

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `discrepancy_reports`
--

DROP TABLE IF EXISTS `discrepancy_reports`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `discrepancy_reports` (
  `Report_ID` int NOT NULL AUTO_INCREMENT,
  `PO_ID` int DEFAULT NULL,
  `Product_ID` int DEFAULT NULL,
  `Expected_QTY` int DEFAULT NULL,
  `Actual_QTY` int DEFAULT NULL,
  `Reason_Code` varchar(45) DEFAULT NULL,
  `Reported_By` int DEFAULT NULL,
  PRIMARY KEY (`Report_ID`),
  KEY `fk_PO_ID_idx` (`PO_ID`),
  KEY `fk_Product_ID_idx` (`Product_ID`),
  KEY `fk2_User_ID_idx` (`Reported_By`),
  CONSTRAINT `fk2_PO_ID` FOREIGN KEY (`PO_ID`) REFERENCES `purchase_orders` (`PO_ID`),
  CONSTRAINT `fk2_Product_ID` FOREIGN KEY (`Product_ID`) REFERENCES `products` (`Product_ID`),
  CONSTRAINT `fk2_User_ID` FOREIGN KEY (`Reported_By`) REFERENCES `users` (`User_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `discrepancy_reports`
--

LOCK TABLES `discrepancy_reports` WRITE;
/*!40000 ALTER TABLE `discrepancy_reports` DISABLE KEYS */;
INSERT INTO `discrepancy_reports` VALUES (2,5,1,24,20,'Damaged',1),(3,5,2,24,23,'Damaged',1),(4,17,8,24,23,'Damaged',1),(5,17,8,24,20,'Missing',1),(6,17,8,24,4,'Missing',1),(7,21,9,13,12,'Damaged',1);
/*!40000 ALTER TABLE `discrepancy_reports` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_items`
--

DROP TABLE IF EXISTS `order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_items` (
  `PO_ID` int NOT NULL,
  `Product_ID` int NOT NULL,
  `Quantity_Ordered` int DEFAULT NULL,
  `Quantity_Received` int DEFAULT NULL,
  PRIMARY KEY (`PO_ID`,`Product_ID`),
  KEY `fk_Product_ID_idx` (`Product_ID`),
  CONSTRAINT `fk_PO_ID` FOREIGN KEY (`PO_ID`) REFERENCES `purchase_orders` (`PO_ID`),
  CONSTRAINT `fk_Product_ID` FOREIGN KEY (`Product_ID`) REFERENCES `products` (`Product_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_items`
--

LOCK TABLES `order_items` WRITE;
/*!40000 ALTER TABLE `order_items` DISABLE KEYS */;
INSERT INTO `order_items` VALUES (5,1,24,20),(5,2,24,23),(17,8,24,4),(18,8,1,1),(19,7,12,12),(20,6,13,13),(21,9,13,12),(22,9,1,1),(23,10,13,13),(24,11,13,13);
/*!40000 ALTER TABLE `order_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `Product_ID` int NOT NULL AUTO_INCREMENT,
  `Product_Name` varchar(45) DEFAULT NULL,
  `Barcode` varchar(45) DEFAULT NULL,
  `Current_Stock_Level` int DEFAULT NULL,
  `Reorder_Level` int DEFAULT NULL,
  `Max_Level` int DEFAULT NULL,
  PRIMARY KEY (`Product_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,'Coca Cola Reg 500ml','123456',72,24,72),(2,'Coca Cola Zero 500ml','456789',50,24,72),(6,'Diet Coke 500ml','247843270',25,24,72),(7,'Fanta Orange 500ml','979378934',36,24,48),(8,'Fanta Lemon 500ml','894216786',48,24,48),(9,'Lucozade Sport Orange 500ml','34782649382',25,24,72),(10,'Coke can','765765',25,24,72),(11,'Diet Coke Can','34325',25,24,72);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase_orders`
--

DROP TABLE IF EXISTS `purchase_orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase_orders` (
  `PO_ID` int NOT NULL AUTO_INCREMENT,
  `Supplier_ID` int DEFAULT NULL,
  `Status` varchar(45) DEFAULT NULL,
  `TimeStamp` datetime DEFAULT NULL,
  `Manager_ID` int DEFAULT NULL,
  PRIMARY KEY (`PO_ID`),
  KEY `Supplier_ID_idx` (`Supplier_ID`),
  KEY `fk_Manager_ID_idx` (`Manager_ID`),
  CONSTRAINT `fk_Manager_ID` FOREIGN KEY (`Manager_ID`) REFERENCES `users` (`User_ID`),
  CONSTRAINT `Supplier_ID` FOREIGN KEY (`Supplier_ID`) REFERENCES `suppliers` (`Supplier_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase_orders`
--

LOCK TABLES `purchase_orders` WRITE;
/*!40000 ALTER TABLE `purchase_orders` DISABLE KEYS */;
INSERT INTO `purchase_orders` VALUES (5,1,'Arrived','2026-02-16 11:39:15',1),(8,1,'Arrived','2026-03-10 09:30:58',1),(15,1,'Arrived','2026-04-12 13:42:22',1),(16,2,'Arrived','2026-04-20 10:21:59',1),(17,2,'Arrived','2026-04-20 10:25:33',1),(18,2,'Arrived','2026-04-20 10:34:16',1),(19,4,'Arrived','2026-04-20 10:35:48',1),(20,1,'Arrived','2026-04-21 09:13:23',1),(21,5,'Arrived','2026-04-21 09:13:23',1),(22,5,'Arrived','2026-04-21 09:17:33',1),(23,2,'Arrived','2026-04-21 19:56:07',1),(24,5,'Arrived','2026-04-21 19:56:07',1);
/*!40000 ALTER TABLE `purchase_orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `supplier_products`
--

DROP TABLE IF EXISTS `supplier_products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `supplier_products` (
  `Product_ID` int NOT NULL,
  `Supplier_ID` int DEFAULT NULL,
  `cost_price` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`Product_ID`),
  KEY `Supplier_ID_idx` (`Supplier_ID`),
  CONSTRAINT `fk_Supplier_ID` FOREIGN KEY (`Supplier_ID`) REFERENCES `suppliers` (`Supplier_ID`),
  CONSTRAINT `Product_ID` FOREIGN KEY (`Product_ID`) REFERENCES `products` (`Product_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `supplier_products`
--

LOCK TABLES `supplier_products` WRITE;
/*!40000 ALTER TABLE `supplier_products` DISABLE KEYS */;
INSERT INTO `supplier_products` VALUES (1,1,0.85),(2,1,0.90),(6,1,0.80),(7,4,0.86),(8,2,0.67),(9,5,0.70),(10,2,0.55),(11,5,0.50);
/*!40000 ALTER TABLE `supplier_products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `suppliers`
--

DROP TABLE IF EXISTS `suppliers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `suppliers` (
  `Supplier_ID` int NOT NULL AUTO_INCREMENT,
  `Supplier_Name` varchar(45) DEFAULT NULL,
  `Supplier_Email` varchar(45) DEFAULT NULL,
  `Supplier_Phone` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`Supplier_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `suppliers`
--

LOCK TABLES `suppliers` WRITE;
/*!40000 ALTER TABLE `suppliers` DISABLE KEYS */;
INSERT INTO `suppliers` VALUES (1,'Sample','sample@gmail.com','1234567'),(2,'test','Test@gmail.com','1160179'),(4,'John','John@gmail.com','36782462'),(5,'James','James@gmail.com','48324736');
/*!40000 ALTER TABLE `suppliers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `User_ID` int NOT NULL AUTO_INCREMENT,
  `Username` varchar(45) DEFAULT NULL,
  `Password` varchar(255) DEFAULT NULL,
  `Is_Manager` tinyint NOT NULL,
  PRIMARY KEY (`User_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Test','Test',1),(2,'Test2','Test2',0),(3,'Test3','Test3',0),(4,'Test4','Test4',1),(5,'Test5','Test5',0),(6,'Claudius','Fart',1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-21 22:08:47
