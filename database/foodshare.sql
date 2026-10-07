-- MySQL dump 10.13  Distrib 26.7.0, for Win64 (x86_64)
--
-- Host: localhost    Database: foodshare
-- ------------------------------------------------------
-- Server version	26.7.0

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
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ 'cbd67de6-ad82-11f1-a55a-a027e1f31fc0:1-162';

--
-- Table structure for table `food_items`
--

DROP TABLE IF EXISTS `food_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `food_items` (
  `food_id` int NOT NULL AUTO_INCREMENT,
  `donor_id` int NOT NULL,
  `food_name` varchar(100) NOT NULL,
  `quantity` int NOT NULL,
  `food_type` varchar(50) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `image_path` varchar(255) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `available_until` datetime DEFAULT NULL,
  `donation_type` enum('FREE','LOW_COST') NOT NULL,
  `price` decimal(10,2) DEFAULT '0.00',
  `status` enum('AVAILABLE','REQUESTED','COMPLETED','EXPIRED') DEFAULT 'AVAILABLE',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`food_id`),
  KEY `donor_id` (`donor_id`),
  CONSTRAINT `food_items_ibfk_1` FOREIGN KEY (`donor_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `food_items`
--

LOCK TABLES `food_items` WRITE;
/*!40000 ALTER TABLE `food_items` DISABLE KEYS */;
INSERT INTO `food_items` VALUES (1,1,'rice',10,'Vegetarian','freah','uploads/fried-chicken.png','hyderabad','2026-10-04 20:54:00','FREE',0.00,'AVAILABLE','2026-10-04 15:24:38'),(2,1,'burger',3,'Non-Vegetarian','fresh food','uploads/lasagna.png','rajahmundry','2026-10-05 12:06:00','FREE',0.00,'AVAILABLE','2026-10-05 06:36:52'),(3,1,'burger',2,'Non-Vegetarian','frdsgrddr','uploads/burger.png','hyderabad','2026-10-06 12:16:00','FREE',0.00,'AVAILABLE','2026-10-05 06:46:36'),(4,1,'burger',1,'Non-Vegetarian','fresh food','uploads/burger.png','rajahmundry','2026-10-05 12:24:00','FREE',0.00,'AVAILABLE','2026-10-05 06:54:35');
/*!40000 ALTER TABLE `food_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `food_requests`
--

DROP TABLE IF EXISTS `food_requests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `food_requests` (
  `request_id` int NOT NULL AUTO_INCREMENT,
  `food_id` int NOT NULL,
  `receiver_id` int NOT NULL,
  `quantity` int NOT NULL,
  `status` enum('PENDING','ACCEPTED','REJECTED','COMPLETED') DEFAULT 'PENDING',
  `requested_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`request_id`),
  KEY `food_id` (`food_id`),
  KEY `receiver_id` (`receiver_id`),
  CONSTRAINT `food_requests_ibfk_1` FOREIGN KEY (`food_id`) REFERENCES `food_items` (`food_id`),
  CONSTRAINT `food_requests_ibfk_2` FOREIGN KEY (`receiver_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `food_requests`
--

LOCK TABLES `food_requests` WRITE;
/*!40000 ALTER TABLE `food_requests` DISABLE KEYS */;
INSERT INTO `food_requests` VALUES (1,1,2,4,'ACCEPTED','2026-10-04 16:48:24'),(2,1,2,1,'REJECTED','2026-10-05 06:16:58'),(3,2,2,1,'PENDING','2026-10-05 06:39:49'),(4,3,2,1,'REJECTED','2026-10-05 06:48:47'),(5,2,2,2,'ACCEPTED','2026-10-05 06:49:05'),(6,4,2,1,'ACCEPTED','2026-10-05 06:55:22'),(7,4,2,1,'PENDING','2026-10-05 15:42:28'),(8,4,2,1,'PENDING','2026-10-05 16:12:47'),(9,3,2,5,'ACCEPTED','2026-10-06 08:54:16'),(10,3,2,6,'ACCEPTED','2026-10-06 09:08:24');
/*!40000 ALTER TABLE `food_requests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pickups`
--

DROP TABLE IF EXISTS `pickups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pickups` (
  `pickup_id` int NOT NULL AUTO_INCREMENT,
  `request_id` int NOT NULL,
  `volunteer_id` int NOT NULL,
  `pickup_status` enum('ASSIGNED','PICKED_UP','DELIVERED') DEFAULT 'ASSIGNED',
  `pickup_time` datetime DEFAULT NULL,
  `delivery_time` datetime DEFAULT NULL,
  PRIMARY KEY (`pickup_id`),
  KEY `request_id` (`request_id`),
  KEY `volunteer_id` (`volunteer_id`),
  CONSTRAINT `pickups_ibfk_1` FOREIGN KEY (`request_id`) REFERENCES `food_requests` (`request_id`),
  CONSTRAINT `pickups_ibfk_2` FOREIGN KEY (`volunteer_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pickups`
--

LOCK TABLES `pickups` WRITE;
/*!40000 ALTER TABLE `pickups` DISABLE KEYS */;
INSERT INTO `pickups` VALUES (1,1,3,'DELIVERED','2026-10-05 10:36:23','2026-10-05 10:59:37'),(3,5,3,'DELIVERED','2026-10-05 12:21:03','2026-10-05 12:21:10'),(4,6,3,'DELIVERED','2026-10-05 12:26:47','2026-10-05 12:26:51');
/*!40000 ALTER TABLE `pickups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `role` enum('DONOR','RECEIVER','VOLUNTEER','ADMIN') NOT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Monica','monica@gmail.com','12345','DONOR','9876543210','Hyderabad'),(2,'Akula Monica Lakshmi sri','akulamonicalakshmisri@gmail.com','56788765738660','DONOR','7603262299','rajahmundry'),(3,'test user','testuser@gamil.com','test123','RECEIVER','9874563210','rajahmundry'),(4,'donor test','donortest@gamil.com','donor123','DONOR','9874563210','rajahmundry'),(7,'swathi','donorswathi@123','swathi123','DONOR','8965471230','rajahmundry'),(8,'swarna','receiverswarna@123','swarna123','RECEIVER','9874563210','rajahmundry'),(9,'gaaya','gaaya@123','gaaya123','VOLUNTEER','8965471230','rajahmundry');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-07 10:01:10
