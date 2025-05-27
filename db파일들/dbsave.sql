-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: tableup
-- ------------------------------------------------------
-- Server version	8.0.41

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
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `ad_num` int NOT NULL AUTO_INCREMENT,
  `ad_id` varchar(20) NOT NULL,
  `ad_pw` varchar(255) NOT NULL,
  PRIMARY KEY (`ad_num`),
  UNIQUE KEY `ad_id` (`ad_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES (1,'a123','$2a$10$juSqcXcXvHzpTdW1Ml/yk.IaLl7uPP77lksZan0T83ZH8Dq4ns7MG');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `businessdate`
--

DROP TABLE IF EXISTS `businessdate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `businessdate` (
  `BD_NUM` int NOT NULL AUTO_INCREMENT,
  `BD_DATE` date NOT NULL,
  `bd_rt_num` int DEFAULT NULL,
  `BD_OFF` tinyint(1) NOT NULL,
  `bd_open` datetime DEFAULT NULL,
  `bd_close` datetime DEFAULT NULL,
  `bd_brstart` datetime DEFAULT NULL,
  `bd_brend` datetime DEFAULT NULL,
  `bd_loam` datetime DEFAULT NULL,
  `bd_lopm` datetime DEFAULT NULL,
  PRIMARY KEY (`BD_NUM`),
  KEY `FK_Restaurant_TO_BusinessDate_1` (`bd_rt_num`),
  CONSTRAINT `FK_Restaurant_TO_BusinessDate_1` FOREIGN KEY (`bd_rt_num`) REFERENCES `restaurant` (`rt_num`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `businessdate`
--

LOCK TABLES `businessdate` WRITE;
/*!40000 ALTER TABLE `businessdate` DISABLE KEYS */;
INSERT INTO `businessdate` VALUES (1,'1923-03-12',2,0,'1970-01-01 10:59:00','1970-01-01 22:02:00',NULL,NULL,'1970-01-01 10:57:00','1970-01-01 21:56:00'),(2,'2025-05-23',1,1,NULL,NULL,NULL,NULL,NULL,NULL),(3,'2025-05-07',1,0,'2025-05-07 14:50:00','2025-05-07 13:52:00',NULL,NULL,NULL,'2025-05-07 15:51:00'),(4,'2025-05-07',1,0,'2025-05-07 16:32:00','2025-05-07 07:34:00','2025-05-07 05:33:00','2025-05-07 04:34:00','2025-05-07 06:33:00','2025-05-07 15:34:00'),(5,'2025-05-07',1,0,'2025-05-07 10:10:00','2025-05-07 21:11:00','2025-05-07 13:11:00','2025-05-07 15:11:00','2025-05-07 13:00:00','2025-05-07 21:00:00'),(6,'2025-05-08',1,0,'2025-05-08 07:12:00','2025-05-08 19:13:00',NULL,NULL,NULL,'2025-05-08 18:14:00');
/*!40000 ALTER TABLE `businessdate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `businesshour`
--

DROP TABLE IF EXISTS `businesshour`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `businesshour` (
  `bh_num` int NOT NULL AUTO_INCREMENT,
  `bh_start` datetime NOT NULL,
  `bh_end` datetime DEFAULT NULL,
  `bh_seat_MAX` int DEFAULT NULL,
  `bh_seat_current` int DEFAULT NULL,
  `bh_table_MAX` int DEFAULT NULL,
  `bh_table_current` int DEFAULT NULL,
  `bh_state` tinyint(1) DEFAULT NULL,
  `bh_rt_num` int DEFAULT NULL,
  PRIMARY KEY (`bh_num`),
  KEY `FK_Restaurant_TO_BusinessHour_1` (`bh_rt_num`),
  CONSTRAINT `FK_Restaurant_TO_BusinessHour_1` FOREIGN KEY (`bh_rt_num`) REFERENCES `restaurant` (`rt_num`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `businesshour`
--

LOCK TABLES `businesshour` WRITE;
/*!40000 ALTER TABLE `businesshour` DISABLE KEYS */;
INSERT INTO `businesshour` VALUES (1,'1970-01-01 12:23:00','1970-01-01 14:00:00',5,0,5,0,0,1),(2,'1970-01-01 01:19:00','1970-01-01 02:20:00',5,0,5,0,0,1),(3,'2025-05-27 01:05:00','2025-05-27 01:06:00',5,0,5,0,0,1),(4,'2025-05-27 01:10:00','2025-05-27 01:10:00',5,0,5,0,0,1),(5,'2025-05-27 01:11:00','2025-05-27 00:10:00',5,0,5,0,0,1);
/*!40000 ALTER TABLE `businesshour` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `businesshourtemplate`
--

DROP TABLE IF EXISTS `businesshourtemplate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `businesshourtemplate` (
  `bhd_num` int NOT NULL AUTO_INCREMENT,
  `bhd_date` enum('월','화','수','목','금','토','일') NOT NULL,
  `bhd_timeStart` time NOT NULL,
  `bhd_timeEnd` time NOT NULL,
  `bhd_seat` int NOT NULL,
  `bhd_table` int NOT NULL,
  `bhd_rt_num` int DEFAULT NULL,
  PRIMARY KEY (`bhd_num`),
  KEY `FK_Restaurant_TO_BusinessHourTemplate_1` (`bhd_rt_num`),
  CONSTRAINT `FK_Restaurant_TO_BusinessHourTemplate_1` FOREIGN KEY (`bhd_rt_num`) REFERENCES `restaurant` (`rt_num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `businesshourtemplate`
--

LOCK TABLES `businesshourtemplate` WRITE;
/*!40000 ALTER TABLE `businesshourtemplate` DISABLE KEYS */;
/*!40000 ALTER TABLE `businesshourtemplate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart`
--

DROP TABLE IF EXISTS `cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart` (
  `cart_num` int NOT NULL AUTO_INCREMENT,
  `cart_MN_NUM` int NOT NULL,
  `cart_res_num` int DEFAULT NULL,
  PRIMARY KEY (`cart_num`),
  KEY `FK_MENU_TO_cart_1` (`cart_MN_NUM`),
  KEY `FK_Reservation_TO_cart_1` (`cart_res_num`),
  CONSTRAINT `FK_MENU_TO_cart_1` FOREIGN KEY (`cart_MN_NUM`) REFERENCES `menu` (`MN_NUM`),
  CONSTRAINT `FK_Reservation_TO_cart_1` FOREIGN KEY (`cart_res_num`) REFERENCES `reservation` (`res_num`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart`
--

LOCK TABLES `cart` WRITE;
/*!40000 ALTER TABLE `cart` DISABLE KEYS */;
INSERT INTO `cart` VALUES (1,1,1);
/*!40000 ALTER TABLE `cart` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `defaultrestime`
--

DROP TABLE IF EXISTS `defaultrestime`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `defaultrestime` (
  `DRT_NUM` int NOT NULL AUTO_INCREMENT,
  `drt_rt_num` int NOT NULL,
  `drt_date` enum('월','화','수','목','금','토','일') NOT NULL,
  `drt_off` tinyint(1) NOT NULL,
  `drt_open` time DEFAULT NULL,
  `drt_close` time DEFAULT NULL,
  `drt_brstart` time DEFAULT NULL,
  `drt_brend` time DEFAULT NULL,
  `drt_loam` time DEFAULT NULL,
  `drt_lopm` time DEFAULT NULL,
  PRIMARY KEY (`DRT_NUM`),
  UNIQUE KEY `drt_rt_num` (`drt_rt_num`),
  CONSTRAINT `FK_Restaurant_TO_DefaultResTime_1` FOREIGN KEY (`drt_rt_num`) REFERENCES `restaurant` (`rt_num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `defaultrestime`
--

LOCK TABLES `defaultrestime` WRITE;
/*!40000 ALTER TABLE `defaultrestime` DISABLE KEYS */;
/*!40000 ALTER TABLE `defaultrestime` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detailfoodcategory`
--

DROP TABLE IF EXISTS `detailfoodcategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detailfoodcategory` (
  `dfc_num` int NOT NULL AUTO_INCREMENT,
  `dfc_sub` varchar(50) NOT NULL,
  `dfc_fc_num` int DEFAULT NULL,
  PRIMARY KEY (`dfc_num`),
  KEY `FK_FoodCategory_TO_DetailFoodCategory_1` (`dfc_fc_num`),
  CONSTRAINT `FK_FoodCategory_TO_DetailFoodCategory_1` FOREIGN KEY (`dfc_fc_num`) REFERENCES `foodcategory` (`fc_num`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detailfoodcategory`
--

LOCK TABLES `detailfoodcategory` WRITE;
/*!40000 ALTER TABLE `detailfoodcategory` DISABLE KEYS */;
INSERT INTO `detailfoodcategory` VALUES (1,'찌개류',1),(2,'초밥',2),(3,'다이닝',3),(4,'짜장',4),(5,'뷔페',5),(6,'베트남',6),(7,'카페',7);
/*!40000 ALTER TABLE `detailfoodcategory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detailregion`
--

DROP TABLE IF EXISTS `detailregion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detailregion` (
  `dreg_num` int NOT NULL AUTO_INCREMENT,
  `dreg_sub` varchar(50) NOT NULL,
  `dreg_reg_num` int DEFAULT NULL,
  PRIMARY KEY (`dreg_num`),
  KEY `FK_Region_TO_DetailRegion_1` (`dreg_reg_num`),
  CONSTRAINT `FK_Region_TO_DetailRegion_1` FOREIGN KEY (`dreg_reg_num`) REFERENCES `region` (`reg_num`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detailregion`
--

LOCK TABLES `detailregion` WRITE;
/*!40000 ALTER TABLE `detailregion` DISABLE KEYS */;
INSERT INTO `detailregion` VALUES (1,'강남/서초',1),(2,'잠실/송파/강동',1),(3,'여의도/영등포/강서',1),(4,'건대/성수/왕십리',1),(5,'종로/중구',1),(6,'홍대/합정/마포',1),(7,'용산/이태원/한남',1),(8,'성북/노량/중랑',1),(9,'구로/관악/동작',1),(10,'기타',1),(11,'경기 북부',2),(12,'의왕/안양/군포',2),(13,'용인/화성/평택',2),(14,'부천/시흥/안산',2),(15,'성남/하남',2),(16,'수원',2),(17,'인천',2),(18,'기타',2),(19,'부산',3),(20,'울산',3),(21,'경남',3),(22,'대구',3),(23,'경북',3),(24,'울산',3),(25,'기타',3),(26,'제주시',4),(27,'서귀포',4),(28,'기타',4),(29,'춘천',5),(30,'원주',5),(31,'강릉/속초',5),(32,'기타',5),(33,'대전',6),(34,'세종',6),(35,'충남',6),(36,'충북',6),(37,'기타',6),(38,'광주',7),(39,'전주',7),(40,'전남',7),(41,'전북',7),(42,'기타',7);
/*!40000 ALTER TABLE `detailregion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `facility`
--

DROP TABLE IF EXISTS `facility`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `facility` (
  `fa_num` int NOT NULL AUTO_INCREMENT,
  `fa_name` varchar(50) NOT NULL,
  `fa_title` varchar(50) DEFAULT NULL,
  `fa_icon` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`fa_num`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `facility`
--

LOCK TABLES `facility` WRITE;
/*!40000 ALTER TABLE `facility` DISABLE KEYS */;
INSERT INTO `facility` VALUES (1,'무선인터넷','사용가능한 무선인터넷',NULL);
/*!40000 ALTER TABLE `facility` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `file`
--

DROP TABLE IF EXISTS `file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `file` (
  `file_num` int NOT NULL AUTO_INCREMENT,
  `file_path` varchar(255) NOT NULL,
  `file_name` varchar(255) NOT NULL,
  `File_type` enum('REVIEW','RESTAURANTDETAIL','MENU') NOT NULL,
  `File_FOREIGN` int NOT NULL,
  `file_tag` enum('내부','외부','메뉴판','음식','기타') DEFAULT NULL,
  `FILE_RES_NUM` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`file_num`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file`
--

LOCK TABLES `file` WRITE;
/*!40000 ALTER TABLE `file` DISABLE KEYS */;
INSERT INTO `file` VALUES (1,'/2025/05/26/8569d5a8-22b1-426d-b454-e9d0b19d9b4d_cap3.jpg','cap3.jpg','RESTAURANTDETAIL',3,'내부',3),(2,'/2025/05/26/0a7f4ffd-e224-4200-9530-0eb6772da023_cap1.png','cap1.png','RESTAURANTDETAIL',3,'내부',3);
/*!40000 ALTER TABLE `file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `foodcategory`
--

DROP TABLE IF EXISTS `foodcategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `foodcategory` (
  `fc_num` int NOT NULL AUTO_INCREMENT,
  `fc_main` varchar(50) NOT NULL,
  PRIMARY KEY (`fc_num`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `foodcategory`
--

LOCK TABLES `foodcategory` WRITE;
/*!40000 ALTER TABLE `foodcategory` DISABLE KEYS */;
INSERT INTO `foodcategory` VALUES (1,'한식'),(2,'일식'),(3,'양식'),(4,'중식'),(5,'뷔페/무한리필'),(6,'아시아'),(7,'카페');
/*!40000 ALTER TABLE `foodcategory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu`
--

DROP TABLE IF EXISTS `menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menu` (
  `MN_NUM` int NOT NULL AUTO_INCREMENT,
  `MN_NAME` varchar(50) NOT NULL,
  `MN_IMG` varchar(255) DEFAULT NULL,
  `MN_PRICE` int DEFAULT NULL,
  `MN_CONTENT` varchar(50) DEFAULT NULL,
  `MN_MT_NUM` int NOT NULL,
  `mn_rt_num` int DEFAULT NULL,
  `MN_DIV` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`MN_NUM`),
  KEY `FK_MenuType_TO_MENU_1` (`MN_MT_NUM`),
  KEY `FK_Restaurant_TO_MENU_1` (`mn_rt_num`),
  CONSTRAINT `FK_MenuType_TO_MENU_1` FOREIGN KEY (`MN_MT_NUM`) REFERENCES `menutype` (`MT_NUM`),
  CONSTRAINT `FK_Restaurant_TO_MENU_1` FOREIGN KEY (`mn_rt_num`) REFERENCES `restaurant` (`rt_num`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu`
--

LOCK TABLES `menu` WRITE;
/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
INSERT INTO `menu` VALUES (1,'디폴트',NULL,10000,'디폴트',1,1,NULL),(2,'123','menu/2.jpg',123,'123',1,1,'123'),(3,'123','menu/3.jfif',10000,'고기1',1,1,'고기'),(4,'등갈비','menu/4.jfif',5000,'저렴한 등갈비',1,3,'주력메뉴'),(5,'삽겹살','menu/5.jpg',20000,'둘이먹다 하나가 굶어도 모르는 양',1,3,'주력메뉴');
/*!40000 ALTER TABLE `menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menutype`
--

DROP TABLE IF EXISTS `menutype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menutype` (
  `MT_NUM` int NOT NULL AUTO_INCREMENT,
  `MT_NAME` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`MT_NUM`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menutype`
--

LOCK TABLES `menutype` WRITE;
/*!40000 ALTER TABLE `menutype` DISABLE KEYS */;
INSERT INTO `menutype` VALUES (1,'고기'),(2,'찌개류'),(3,'면류'),(4,'음료'),(5,'주류'),(6,'튀김류'),(7,'돈까스');
/*!40000 ALTER TABLE `menutype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment`
--

DROP TABLE IF EXISTS `payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment` (
  `pay_num` int NOT NULL AUTO_INCREMENT,
  `pay_res_num` int DEFAULT NULL,
  `pay_method` varchar(50) DEFAULT NULL,
  `pay_amount` int DEFAULT NULL,
  `pay_status` enum('Y','N','C') DEFAULT NULL,
  `pay_time` datetime DEFAULT NULL,
  PRIMARY KEY (`pay_num`),
  KEY `FK_Reservation_TO_Payment_1` (`pay_res_num`),
  CONSTRAINT `FK_Reservation_TO_Payment_1` FOREIGN KEY (`pay_res_num`) REFERENCES `reservation` (`res_num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment`
--

LOCK TABLES `payment` WRITE;
/*!40000 ALTER TABLE `payment` DISABLE KEYS */;
/*!40000 ALTER TABLE `payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `region`
--

DROP TABLE IF EXISTS `region`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `region` (
  `reg_num` int NOT NULL AUTO_INCREMENT,
  `reg_main` varchar(50) NOT NULL,
  PRIMARY KEY (`reg_num`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `region`
--

LOCK TABLES `region` WRITE;
/*!40000 ALTER TABLE `region` DISABLE KEYS */;
INSERT INTO `region` VALUES (1,'서울'),(2,'경기/인천'),(3,'경상'),(4,'제주'),(5,'강원'),(6,'충청'),(7,'전라');
/*!40000 ALTER TABLE `region` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rescoupon`
--

DROP TABLE IF EXISTS `rescoupon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rescoupon` (
  `ReC_NUM` int NOT NULL AUTO_INCREMENT,
  `ReC_Content` longtext NOT NULL,
  `Rec_state` tinyint(1) NOT NULL,
  `REC_period` datetime DEFAULT NULL,
  `rec_rt_num` int DEFAULT NULL,
  PRIMARY KEY (`ReC_NUM`),
  KEY `FK_Restaurant_TO_ResCoupon_1` (`rec_rt_num`),
  CONSTRAINT `FK_Restaurant_TO_ResCoupon_1` FOREIGN KEY (`rec_rt_num`) REFERENCES `restaurant` (`rt_num`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rescoupon`
--

LOCK TABLES `rescoupon` WRITE;
/*!40000 ALTER TABLE `rescoupon` DISABLE KEYS */;
INSERT INTO `rescoupon` VALUES (1,'쿠폰설명',0,'2025-06-13 16:56:02',1),(2,'쿠폰1',1,'2025-10-12 00:00:00',3),(3,'쿠폰1',1,'2025-10-12 00:00:00',1);
/*!40000 ALTER TABLE `rescoupon` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservation`
--

DROP TABLE IF EXISTS `reservation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservation` (
  `res_num` int NOT NULL AUTO_INCREMENT,
  `res_us_num` int NOT NULL,
  `res_rt_num` int DEFAULT NULL,
  `res_time` datetime NOT NULL,
  `res_person` int NOT NULL,
  `res_request` text NOT NULL,
  `res_created` datetime NOT NULL,
  `res_state` int NOT NULL,
  PRIMARY KEY (`res_num`),
  KEY `FK_User_TO_Reservation_1` (`res_us_num`),
  KEY `FK_Restaurant_TO_Reservation_1` (`res_rt_num`),
  CONSTRAINT `FK_Restaurant_TO_Reservation_1` FOREIGN KEY (`res_rt_num`) REFERENCES `restaurant` (`rt_num`),
  CONSTRAINT `FK_User_TO_Reservation_1` FOREIGN KEY (`res_us_num`) REFERENCES `user` (`us_num`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservation`
--

LOCK TABLES `reservation` WRITE;
/*!40000 ALTER TABLE `reservation` DISABLE KEYS */;
INSERT INTO `reservation` VALUES (1,1,1,'2025-05-13 16:56:02',2,'요청사항','2025-05-13 16:56:02',1),(2,1,1,'2025-06-01 16:20:09',2,'요청사항','2025-05-15 16:20:09',1);
/*!40000 ALTER TABLE `reservation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resnews`
--

DROP TABLE IF EXISTS `resnews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `resnews` (
  `RN_NUM` int NOT NULL AUTO_INCREMENT,
  `rn_rt_num` int DEFAULT NULL,
  `rn_content` longtext NOT NULL,
  `rn_state` tinyint(1) NOT NULL,
  PRIMARY KEY (`RN_NUM`),
  KEY `FK_Restaurant_TO_ResNews_1` (`rn_rt_num`),
  CONSTRAINT `FK_Restaurant_TO_ResNews_1` FOREIGN KEY (`rn_rt_num`) REFERENCES `restaurant` (`rt_num`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resnews`
--

LOCK TABLES `resnews` WRITE;
/*!40000 ALTER TABLE `resnews` DISABLE KEYS */;
INSERT INTO `resnews` VALUES (1,1,'매장소식2',0),(2,3,'뉴스1',1),(3,1,'뉴스2',1);
/*!40000 ALTER TABLE `resnews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `restaurant`
--

DROP TABLE IF EXISTS `restaurant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `restaurant` (
  `rt_num` int NOT NULL AUTO_INCREMENT,
  `rt_name` varchar(100) NOT NULL,
  `rt_closed_days` varchar(100) DEFAULT NULL,
  `rt_price_lunch` varchar(20) DEFAULT NULL,
  `rt_price_dinner` varchar(20) DEFAULT NULL,
  `rt_accept` enum('Y','N') NOT NULL,
  `rt_dfc_num` int DEFAULT NULL,
  `rt_dreg_num` int DEFAULT NULL,
  `rt_description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`rt_num`),
  KEY `FK_DetailFoodCategory_TO_Restaurant_1` (`rt_dfc_num`),
  KEY `FK_DetailRegion_TO_Restaurant_1` (`rt_dreg_num`),
  CONSTRAINT `FK_DetailFoodCategory_TO_Restaurant_1` FOREIGN KEY (`rt_dfc_num`) REFERENCES `detailfoodcategory` (`dfc_num`),
  CONSTRAINT `FK_DetailRegion_TO_Restaurant_1` FOREIGN KEY (`rt_dreg_num`) REFERENCES `detailregion` (`dreg_num`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `restaurant`
--

LOCK TABLES `restaurant` WRITE;
/*!40000 ALTER TABLE `restaurant` DISABLE KEYS */;
INSERT INTO `restaurant` VALUES (1,'디폴트 매장','화요일','10000','20000','Y',1,1,'테스트용 매장'),(2,'디폴트 매장2','수요일','5000','7000','Y',3,4,'테스트용 매장2'),(3,'매장2','화요일','11000','21000','N',2,2,'<p>디폴트 매장 2입니다</p>');
/*!40000 ALTER TABLE `restaurant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `restaurantdate`
--

DROP TABLE IF EXISTS `restaurantdate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `restaurantdate` (
  `rd_num` int NOT NULL AUTO_INCREMENT,
  `rd_rt_num` int DEFAULT NULL,
  `rd_day` varchar(50) DEFAULT NULL,
  `rd_time` varchar(50) DEFAULT NULL,
  `rd_type` enum('오전','오후','브레이크타임','라스트오더') DEFAULT NULL,
  PRIMARY KEY (`rd_num`),
  KEY `FK_Restaurant_TO_RestaurantDate_1` (`rd_rt_num`),
  CONSTRAINT `FK_Restaurant_TO_RestaurantDate_1` FOREIGN KEY (`rd_rt_num`) REFERENCES `restaurant` (`rt_num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `restaurantdate`
--

LOCK TABLES `restaurantdate` WRITE;
/*!40000 ALTER TABLE `restaurantdate` DISABLE KEYS */;
/*!40000 ALTER TABLE `restaurantdate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `restaurantdetail`
--

DROP TABLE IF EXISTS `restaurantdetail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `restaurantdetail` (
  `RD_NUM` int NOT NULL AUTO_INCREMENT,
  `rd_rt_num` int DEFAULT NULL,
  `rd_phone` varchar(255) DEFAULT NULL,
  `rd_closed_days` varchar(255) DEFAULT NULL,
  `rd_info` varchar(255) DEFAULT NULL,
  `rd_home` varchar(255) DEFAULT NULL,
  `rd_addr` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`RD_NUM`),
  KEY `FK_Restaurant_TO_RestaurantDetail_1` (`rd_rt_num`),
  CONSTRAINT `FK_Restaurant_TO_RestaurantDetail_1` FOREIGN KEY (`rd_rt_num`) REFERENCES `restaurant` (`rt_num`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `restaurantdetail`
--

LOCK TABLES `restaurantdetail` WRITE;
/*!40000 ALTER TABLE `restaurantdetail` DISABLE KEYS */;
INSERT INTO `restaurantdetail` VALUES (1,1,'010-2345-6789','화요일 휴무','디폴트 안내및 유의사항','디폴트 url','디폴트 주소'),(2,2,'010-2345-6700','수요일 휴무','디폴트 안내및 유의사항','www.naver.com','디폴트 주소2'),(3,3,'010-123412','화요일','<p>없음</p>','www.naver.com','서울특별시 강남구 강남구 테헤란로14길 6');
/*!40000 ALTER TABLE `restaurantdetail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `restaurantfacility`
--

DROP TABLE IF EXISTS `restaurantfacility`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `restaurantfacility` (
  `rf_num` int NOT NULL AUTO_INCREMENT,
  `rf_rt_num` int DEFAULT NULL,
  `rf_fa_num` int DEFAULT NULL,
  `rf_detail` longtext,
  PRIMARY KEY (`rf_num`),
  KEY `FK_Restaurant_TO_RestaurantFacility_1` (`rf_rt_num`),
  KEY `FK_Facility_TO_RestaurantFacility_1` (`rf_fa_num`),
  CONSTRAINT `FK_Facility_TO_RestaurantFacility_1` FOREIGN KEY (`rf_fa_num`) REFERENCES `facility` (`fa_num`),
  CONSTRAINT `FK_Restaurant_TO_RestaurantFacility_1` FOREIGN KEY (`rf_rt_num`) REFERENCES `restaurant` (`rt_num`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `restaurantfacility`
--

LOCK TABLES `restaurantfacility` WRITE;
/*!40000 ALTER TABLE `restaurantfacility` DISABLE KEYS */;
INSERT INTO `restaurantfacility` VALUES (1,1,1,'와이파이 사용 가능(가게 문의)'),(2,3,1,'와이파이 비번 12345678 ');
/*!40000 ALTER TABLE `restaurantfacility` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `restaurantmanager`
--

DROP TABLE IF EXISTS `restaurantmanager`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `restaurantmanager` (
  `rm_num` int NOT NULL AUTO_INCREMENT,
  `rm_id` varchar(50) NOT NULL,
  `rm_pw` varchar(255) NOT NULL,
  `rm_email` varchar(100) NOT NULL,
  `rm_phone` varchar(20) NOT NULL,
  `rm_name` varchar(50) NOT NULL,
  `rm_business` varchar(30) NOT NULL,
  `rm_rt_num` int DEFAULT NULL,
  PRIMARY KEY (`rm_num`),
  KEY `FK_Restaurant_TO_RestaurantManager_1` (`rm_rt_num`),
  CONSTRAINT `FK_Restaurant_TO_RestaurantManager_1` FOREIGN KEY (`rm_rt_num`) REFERENCES `restaurant` (`rt_num`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `restaurantmanager`
--

LOCK TABLES `restaurantmanager` WRITE;
/*!40000 ALTER TABLE `restaurantmanager` DISABLE KEYS */;
INSERT INTO `restaurantmanager` VALUES (1,'m123','$2a$10$juSqcXcXvHzpTdW1Ml/yk.IaLl7uPP77lksZan0T83ZH8Dq4ns7MG','rm@example.com','010-2345-6789','디폴트점주','000-00-00000',1),(2,'123','$2a$10$juSqcXcXvHzpTdW1Ml/yk.IaLl7uPP77lksZan0T83ZH8Dq4ns7MG','123@123','123','123','123',3);
/*!40000 ALTER TABLE `restaurantmanager` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `restauranttag`
--

DROP TABLE IF EXISTS `restauranttag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `restauranttag` (
  `Key` int NOT NULL AUTO_INCREMENT,
  `rt_num` int DEFAULT NULL,
  `tag_num` int DEFAULT NULL,
  PRIMARY KEY (`Key`),
  KEY `FK_Restaurant_TO_RestaurantTag_1` (`rt_num`),
  KEY `FK_Tag_TO_RestaurantTag_1` (`tag_num`),
  CONSTRAINT `FK_Restaurant_TO_RestaurantTag_1` FOREIGN KEY (`rt_num`) REFERENCES `restaurant` (`rt_num`),
  CONSTRAINT `FK_Tag_TO_RestaurantTag_1` FOREIGN KEY (`tag_num`) REFERENCES `tag` (`tag_num`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `restauranttag`
--

LOCK TABLES `restauranttag` WRITE;
/*!40000 ALTER TABLE `restauranttag` DISABLE KEYS */;
INSERT INTO `restauranttag` VALUES (1,1,1),(2,1,2);
/*!40000 ALTER TABLE `restauranttag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `review`
--

DROP TABLE IF EXISTS `review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `review` (
  `rev_num` int NOT NULL AUTO_INCREMENT,
  `rev_us_num` int DEFAULT NULL,
  `rev_rt_num` int DEFAULT NULL,
  `rev_content` longtext,
  `rev_created` datetime NOT NULL,
  `rev_updated` datetime NOT NULL,
  `rev_state` int NOT NULL,
  `rev_visit` datetime DEFAULT NULL,
  `rev_visitor` int DEFAULT NULL,
  PRIMARY KEY (`rev_num`),
  KEY `FK_User_TO_Review_1` (`rev_us_num`),
  KEY `FK_Restaurant_TO_Review_1` (`rev_rt_num`),
  CONSTRAINT `FK_Restaurant_TO_Review_1` FOREIGN KEY (`rev_rt_num`) REFERENCES `restaurant` (`rt_num`),
  CONSTRAINT `FK_User_TO_Review_1` FOREIGN KEY (`rev_us_num`) REFERENCES `user` (`us_num`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `review`
--

LOCK TABLES `review` WRITE;
/*!40000 ALTER TABLE `review` DISABLE KEYS */;
INSERT INTO `review` VALUES (1,1,1,'리뷰 내용','2025-05-13 16:56:02','2025-05-13 16:56:02',0,'2025-05-13 16:56:02',2),(2,1,1,'리뷰 내용','2025-05-15 16:20:03','2025-05-15 16:20:03',0,'2025-05-15 16:20:03',2);
/*!40000 ALTER TABLE `review` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reviewscore`
--

DROP TABLE IF EXISTS `reviewscore`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reviewscore` (
  `rs_num` int NOT NULL AUTO_INCREMENT,
  `rs_rev_num` int DEFAULT NULL,
  `rs_st_num` int NOT NULL,
  `rs_score` int DEFAULT NULL,
  PRIMARY KEY (`rs_num`),
  KEY `FK_Review_TO_ReviewScore_1` (`rs_rev_num`),
  KEY `FK_ScoreType_TO_ReviewScore_1` (`rs_st_num`),
  CONSTRAINT `FK_Review_TO_ReviewScore_1` FOREIGN KEY (`rs_rev_num`) REFERENCES `review` (`rev_num`),
  CONSTRAINT `FK_ScoreType_TO_ReviewScore_1` FOREIGN KEY (`rs_st_num`) REFERENCES `scoretype` (`ST_NUM`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reviewscore`
--

LOCK TABLES `reviewscore` WRITE;
/*!40000 ALTER TABLE `reviewscore` DISABLE KEYS */;
INSERT INTO `reviewscore` VALUES (1,1,1,5),(2,1,2,4),(3,1,3,5),(4,1,4,3);
/*!40000 ALTER TABLE `reviewscore` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `scoretype`
--

DROP TABLE IF EXISTS `scoretype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `scoretype` (
  `ST_NUM` int NOT NULL AUTO_INCREMENT,
  `ST_CATEGORY` varchar(50) NOT NULL,
  PRIMARY KEY (`ST_NUM`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `scoretype`
--

LOCK TABLES `scoretype` WRITE;
/*!40000 ALTER TABLE `scoretype` DISABLE KEYS */;
INSERT INTO `scoretype` VALUES (1,'청결도'),(2,'맛'),(3,'분위기'),(4,'친절도');
/*!40000 ALTER TABLE `scoretype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sociallogin`
--

DROP TABLE IF EXISTS `sociallogin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sociallogin` (
  `sl_num` int NOT NULL AUTO_INCREMENT,
  `sl_us_num` int NOT NULL,
  `sl_kakao` tinyint(1) DEFAULT NULL,
  `sl_phone` tinyint(1) DEFAULT NULL,
  `sl_google` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`sl_num`),
  UNIQUE KEY `sl_us_num` (`sl_us_num`),
  CONSTRAINT `FK_User_TO_Sociallogin_1` FOREIGN KEY (`sl_us_num`) REFERENCES `user` (`us_num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sociallogin`
--

LOCK TABLES `sociallogin` WRITE;
/*!40000 ALTER TABLE `sociallogin` DISABLE KEYS */;
/*!40000 ALTER TABLE `sociallogin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tag` (
  `tag_num` int NOT NULL AUTO_INCREMENT,
  `tag_name` varchar(50) NOT NULL,
  `TAG_TT_NUM` int NOT NULL,
  PRIMARY KEY (`tag_num`),
  KEY `FK_TagType_TO_Tag_1` (`TAG_TT_NUM`),
  CONSTRAINT `FK_TagType_TO_Tag_1` FOREIGN KEY (`TAG_TT_NUM`) REFERENCES `tagtype` (`TT_NUM`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag`
--

LOCK TABLES `tag` WRITE;
/*!40000 ALTER TABLE `tag` DISABLE KEYS */;
INSERT INTO `tag` VALUES (1,'차분한',1),(2,'신나는',1),(3,'세련된',1);
/*!40000 ALTER TABLE `tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tagtype`
--

DROP TABLE IF EXISTS `tagtype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tagtype` (
  `TT_NUM` int NOT NULL AUTO_INCREMENT,
  `TT_NAME` varchar(50) NOT NULL,
  PRIMARY KEY (`TT_NUM`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tagtype`
--

LOCK TABLES `tagtype` WRITE;
/*!40000 ALTER TABLE `tagtype` DISABLE KEYS */;
INSERT INTO `tagtype` VALUES (1,'분위기');
/*!40000 ALTER TABLE `tagtype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `us_num` int NOT NULL AUTO_INCREMENT,
  `us_id` varchar(20) NOT NULL,
  `us_pw` varchar(255) NOT NULL,
  `us_name` varchar(50) NOT NULL,
  `us_phone` varchar(20) NOT NULL,
  `us_email` varchar(50) NOT NULL,
  `us_nickname` varchar(30) NOT NULL,
  `us_sociallogin` tinyint(1) DEFAULT NULL,
  `us_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `us_state` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`us_num`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'123','$2a$10$WacCYT43XrEOLt.tCmAFJuFD0bKlytlM2vbLULDzq3eyXkeAbnkBK','홍길동','010-1234-5678','hong@example.com','길동이',0,'2025-05-13 16:56:02',0),(2,'123123','$2a$10$WacCYT43XrEOLt.tCmAFJuFD0bKlytlM2vbLULDzq3eyXkeAbnkBK','123','123','123@123123','123',NULL,'2025-05-15 10:28:29',0),(3,'123456','$2a$10$gmwcIn6gvgRnYmYTa2ksyuIzTuTJho5WCLQes1XQCdO8gj.5WmgQ2','123','12345','123@123123132','123',NULL,'2025-05-19 11:42:08',0),(4,'sample111','$2a$10$1NYe42rmBYtJ5Z3gb.ot5OZk2Ck7UB68cIXqI.s0OQNGrzkBNxHkW','123','010-1231-2312','123@123','123',NULL,'2025-05-19 11:47:13',0),(5,'12312','$2a$10$WMk/TvflbbuJluwHWDz6/uKzh9ZjDradAXEKrmp08saWNEdtK1i9q','123','123123','123@123123123','123',NULL,'2025-05-19 11:49:29',0),(6,'1','$2a$10$PEe5KQAdOhmIt.n9tx6.R.B7oxOW7KhkqppyAzt/jiLE/8TXkXV0m','123','010-1231-2321','1234@12345','123',NULL,'2025-05-19 11:56:37',0),(7,'1231','$2a$10$cowuXN9weVkp27C1z/7IWOqn13RR5Qr5CXeBc9wR/eZ5s7hZlMabS','123','010-1231-2311','1234@12345123','123',NULL,'2025-05-19 12:03:53',0),(8,'12','$2a$10$SFWgueO0GgWmDYAlXULXoeVSThwWHPBRTcRgxzmlgi457ruug24NS','123','010-1231-2123','123@123123123123','user_852e0909',NULL,'2025-05-19 12:09:41',0);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usercoupon`
--

DROP TABLE IF EXISTS `usercoupon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usercoupon` (
  `UC_NUM` int NOT NULL AUTO_INCREMENT,
  `UC_STATE` tinyint(1) NOT NULL,
  `UC_ReC_NUM` int NOT NULL,
  `UC_us_num` int NOT NULL,
  PRIMARY KEY (`UC_NUM`),
  KEY `FK_ResCoupon_TO_UserCoupon_1` (`UC_ReC_NUM`),
  KEY `FK_User_TO_UserCoupon_1` (`UC_us_num`),
  CONSTRAINT `FK_ResCoupon_TO_UserCoupon_1` FOREIGN KEY (`UC_ReC_NUM`) REFERENCES `rescoupon` (`ReC_NUM`),
  CONSTRAINT `FK_User_TO_UserCoupon_1` FOREIGN KEY (`UC_us_num`) REFERENCES `user` (`us_num`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usercoupon`
--

LOCK TABLES `usercoupon` WRITE;
/*!40000 ALTER TABLE `usercoupon` DISABLE KEYS */;
INSERT INTO `usercoupon` VALUES (1,0,1,1);
/*!40000 ALTER TABLE `usercoupon` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userprofileimage`
--

DROP TABLE IF EXISTS `userprofileimage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `userprofileimage` (
  `upi_num` int NOT NULL AUTO_INCREMENT,
  `upi_us_num` int NOT NULL,
  `upi_file_name` varchar(255) NOT NULL,
  `upi_file_path` varchar(255) DEFAULT NULL,
  `upi_upload_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`upi_num`),
  KEY `upi_us_num` (`upi_us_num`),
  CONSTRAINT `userprofileimage_ibfk_1` FOREIGN KEY (`upi_us_num`) REFERENCES `user` (`us_num`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userprofileimage`
--

LOCK TABLES `userprofileimage` WRITE;
/*!40000 ALTER TABLE `userprofileimage` DISABLE KEYS */;
/*!40000 ALTER TABLE `userprofileimage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usfollow`
--

DROP TABLE IF EXISTS `usfollow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usfollow` (
  `UF_NUM` int NOT NULL AUTO_INCREMENT,
  `uf_us_num` int NOT NULL,
  `uf_TYPE` enum('REVIEW','RESTAURANT') NOT NULL,
  `uf_FOREIGN` int NOT NULL,
  PRIMARY KEY (`UF_NUM`),
  KEY `FK_User_TO_UsFollow_1` (`uf_us_num`),
  CONSTRAINT `FK_User_TO_UsFollow_1` FOREIGN KEY (`uf_us_num`) REFERENCES `user` (`us_num`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usfollow`
--

LOCK TABLES `usfollow` WRITE;
/*!40000 ALTER TABLE `usfollow` DISABLE KEYS */;
INSERT INTO `usfollow` VALUES (1,1,'RESTAURANT',1),(2,1,'REVIEW',1),(3,1,'RESTAURANT',2);
/*!40000 ALTER TABLE `usfollow` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-27 17:53:20
