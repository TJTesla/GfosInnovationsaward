-- MySQL dump 10.13  Distrib 8.0.28, for macos11.6 (x86_64)
--
-- Host: 127.0.0.1    Database: ApplicationManagement
-- ------------------------------------------------------
-- Server version	8.0.28

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
-- Table structure for table `applicant`
--

DROP TABLE IF EXISTS `applicant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `applicant` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(40) NOT NULL,
  `password` varchar(100) NOT NULL,
  `salt` varchar(100) NOT NULL,
  `firstname` varchar(40) DEFAULT NULL,
  `lastname` varchar(40) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `gender` int DEFAULT NULL,
  `pb` varchar(100) DEFAULT NULL,
  `lat` double DEFAULT NULL,
  `lon` double DEFAULT NULL,
  `bday` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `applicant_username_uindex` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `applicant`
--

LOCK TABLES `applicant` WRITE;
/*!40000 ALTER TABLE `applicant` DISABLE KEYS */;
INSERT INTO `applicant` VALUES (1,'Yixue','8f434346648f6b96df89dda901c5176b10a6d83961dd3c1ac88b59b2dc327aa4','','Vivien','Hou',NULL,2,NULL,NULL,NULL,NULL),(12,'TTesla','ce0b8a0eb2c976b3cfed32c653ef6f1ce08006d488b865bb4dd2beaeb555740a','','Theodor','Teslia','theoteslia@gmail.com',1,'uploads/profilepics/applicants/0picture.jpg',51.44046,7.01433,NULL),(15,'brittchen','ce0b8a0eb2c976b3cfed32c653ef6f1ce08006d488b865bb4dd2beaeb555740a','','Britta','Teslia','brittateslia@gmx.de',2,'uploads/profilepics/applicants/brittchen2BInFrontOfRobot.png',51.44046,7.01433,NULL),(17,'tes','ce0b8a0eb2c976b3cfed32c653ef6f1ce08006d488b865bb4dd2beaeb555740a','','Anton','Teslia','teslia@gmx.de',1,'uploads/profilepics/applicants/tesKenKaneki.jpg',51.4398407,7.0275026,NULL),(19,'Muster','ca563452b72fce8b11703b0ccbbf5c5800668323663b43afafd2ca3312a075cc','92f064cb8befb1da1571e8d7a1ff82cf','Max','Mustermann','muster@mail.de',1,'uploads/profilepics/applicants/MusterIMG_9667.JPG',51.4399625,7.0135436,'01.01.1970'),(20,'Anonym','9b6aff65ecf6945910101987777a6348592252ced183cc24d00368a3ad92c798','9f06e2bf14ebc865aa3ae1abbc3e0cd3','Anonym','Anonym','anonym@mail.com',0,'',51.4367956,7.0095003,'01.01.1970');
/*!40000 ALTER TABLE `applicant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `application`
--

DROP TABLE IF EXISTS `application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `application` (
  `userId` int NOT NULL,
  `offerId` int NOT NULL,
  `text` text,
  `status` int NOT NULL DEFAULT '0' COMMENT '0: To be reviewed\n1: Currently being reviewed\n10: Accepted\n11: Declined',
  `resumeId` int DEFAULT NULL,
  `draft` tinyint(1) DEFAULT '1',
  UNIQUE KEY `application_pk` (`userId`,`offerId`),
  KEY `application_status_id_fk` (`status`),
  KEY `application_offer_fk` (`offerId`),
  KEY `application_cv_fk` (`resumeId`),
  CONSTRAINT `application_cv_fk` FOREIGN KEY (`resumeId`) REFERENCES `resumes` (`id`),
  CONSTRAINT `application_offer_fk` FOREIGN KEY (`offerId`) REFERENCES `offer` (`id`),
  CONSTRAINT `application_user_fk` FOREIGN KEY (`userId`) REFERENCES `applicant` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `application`
--

LOCK TABLES `application` WRITE;
/*!40000 ALTER TABLE `application` DISABLE KEYS */;
INSERT INTO `application` VALUES (12,3,'Da hab ich Bock drauf!!!',10,11,1),(15,3,'KA brauche arbeit',0,NULL,1);
/*!40000 ALTER TABLE `application` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employees`
--

DROP TABLE IF EXISTS `employees`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employees` (
  `name` varchar(30) NOT NULL,
  `password` varchar(100) NOT NULL,
  `salt` varchar(100) NOT NULL,
  `registered` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`name`),
  UNIQUE KEY `employees_name_uindex` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employees`
--

LOCK TABLES `employees` WRITE;
/*!40000 ALTER TABLE `employees` DISABLE KEYS */;
INSERT INTO `employees` VALUES ('theotes','7c92ffe94cf82e94aa2cda05a5d8e5b5755bfb9c442aeadc3fca67eb515819ef','cd3f1ea903dca761106be6384a61e267',0);
/*!40000 ALTER TABLE `employees` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `favorites`
--

DROP TABLE IF EXISTS `favorites`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `favorites` (
  `applicantId` int NOT NULL,
  `offerId` int NOT NULL,
  UNIQUE KEY `favorites_offerId_uindex` (`offerId`),
  UNIQUE KEY `favorites_applicantId_offerId_uindex` (`applicantId`,`offerId`),
  CONSTRAINT `favorites_offer_fk` FOREIGN KEY (`offerId`) REFERENCES `offer` (`id`),
  CONSTRAINT `favorites_user_fk` FOREIGN KEY (`applicantId`) REFERENCES `applicant` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `favorites`
--

LOCK TABLES `favorites` WRITE;
/*!40000 ALTER TABLE `favorites` DISABLE KEYS */;
INSERT INTO `favorites` VALUES (12,2),(12,3);
/*!40000 ALTER TABLE `favorites` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fields`
--

DROP TABLE IF EXISTS `fields`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fields` (
  `id` int NOT NULL AUTO_INCREMENT,
  `tag` varchar(40) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `tags_id_uindex` (`id`),
  UNIQUE KEY `tags_tag_uindex` (`tag`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fields`
--

LOCK TABLES `fields` WRITE;
/*!40000 ALTER TABLE `fields` DISABLE KEYS */;
INSERT INTO `fields` VALUES (1,'Bau, Architektur, Vermessung'),(2,'Dienstleistung'),(3,'Elektro'),(4,'Gesundheit'),(5,'IT, Computer'),(6,'Kunst, Kultur, Gestaltung'),(7,'Landwirtschaft, Natur, Umwelt'),(8,'Medien'),(9,'Metal, Maschinenbau'),(10,'Naturwisschenschaften'),(11,'Produktion, Fertigung'),(12,'Soziales, Pädagogik'),(13,'Technik, Technologiefelder'),(14,'Verkehr, Logistik'),(15,'Wirtschaft, Verwaltung');
/*!40000 ALTER TABLE `fields` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `level`
--

DROP TABLE IF EXISTS `level`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `level` (
  `id` int NOT NULL AUTO_INCREMENT,
  `term` varchar(30) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `level_id_uindex` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `level`
--

LOCK TABLES `level` WRITE;
/*!40000 ALTER TABLE `level` DISABLE KEYS */;
INSERT INTO `level` VALUES (1,'Praktikanten'),(2,'Abschlussarbeiten'),(3,'Doktoranden'),(4,'Auszubildende'),(5,'Duales Studium'),(6,'Berufserfahrene');
/*!40000 ALTER TABLE `level` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `offer`
--

DROP TABLE IF EXISTS `offer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `offer` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(50) NOT NULL,
  `tasks` text,
  `qualifications` text,
  `extras` text,
  `field` int DEFAULT NULL,
  `level` int DEFAULT NULL,
  `time` int DEFAULT NULL,
  `lat` double DEFAULT NULL,
  `lon` double DEFAULT NULL,
  `draft` tinyint(1) DEFAULT '1',
  `city` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `offer_tag_fk` (`field`),
  KEY `offer_cat_fk` (`time`),
  KEY `offer_level_fk` (`level`),
  CONSTRAINT `offer_cat_fk` FOREIGN KEY (`time`) REFERENCES `time` (`id`),
  CONSTRAINT `offer_level_fk` FOREIGN KEY (`level`) REFERENCES `level` (`id`),
  CONSTRAINT `offer_tag_fk` FOREIGN KEY (`field`) REFERENCES `fields` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `offer`
--

LOCK TABLES `offer` WRITE;
/*!40000 ALTER TABLE `offer` DISABLE KEYS */;
INSERT INTO `offer` VALUES (2,'Build a PC',NULL,NULL,NULL,3,1,3,52.52,13.405,1,'Berlin'),(3,'Sr Dev',NULL,NULL,NULL,5,6,2,51.4556,7.0116,0,'Essen'),(4,'PR',NULL,NULL,NULL,15,4,2,52.52,13.405,0,'Berlin');
/*!40000 ALTER TABLE `offer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resumes`
--

DROP TABLE IF EXISTS `resumes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `resumes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `path` varchar(100) NOT NULL,
  `name` varchar(60) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `resumes_id_uindex` (`id`),
  UNIQUE KEY `resumes_path_uindex` (`path`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resumes`
--

LOCK TABLES `resumes` WRITE;
/*!40000 ALTER TABLE `resumes` DISABLE KEYS */;
INSERT INTO `resumes` VALUES (10,'uploads/resumes/TTesla/4Deutsch.pdf','Deutsch.pdf'),(11,'uploads/resumes/TTesla/3Deutsch.pdf','Deutsch.pdf');
/*!40000 ALTER TABLE `resumes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `status`
--

DROP TABLE IF EXISTS `status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `status` (
  `id` int NOT NULL,
  `description` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `status_description_uindex` (`description`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `status`
--

LOCK TABLES `status` WRITE;
/*!40000 ALTER TABLE `status` DISABLE KEYS */;
INSERT INTO `status` VALUES (3,'Accepted'),(2,'Currently being reviewed'),(4,'Declined'),(0,'Not available'),(1,'To be reviewed');
/*!40000 ALTER TABLE `status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `time`
--

DROP TABLE IF EXISTS `time`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `time` (
  `id` int NOT NULL AUTO_INCREMENT,
  `term` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `category_id_uindex` (`id`),
  UNIQUE KEY `category_term_uindex` (`term`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `time`
--

LOCK TABLES `time` WRITE;
/*!40000 ALTER TABLE `time` DISABLE KEYS */;
INSERT INTO `time` VALUES (3,'Minijob'),(4,'Praktikum'),(1,'Teilzeit'),(2,'Vollzeit');
/*!40000 ALTER TABLE `time` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `title`
--

DROP TABLE IF EXISTS `title`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `title` (
  `id` int NOT NULL AUTO_INCREMENT,
  `term` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `title_id_uindex` (`id`),
  UNIQUE KEY `title_term_uindex` (`term`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `title`
--

LOCK TABLES `title` WRITE;
/*!40000 ALTER TABLE `title` DISABLE KEYS */;
INSERT INTO `title` VALUES (6,'Dipl.-Ing.'),(4,'Dr.'),(5,'Prof.');
/*!40000 ALTER TABLE `title` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `titleRelation`
--

DROP TABLE IF EXISTS `titleRelation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `titleRelation` (
  `applicantId` int NOT NULL,
  `titleId` int NOT NULL,
  PRIMARY KEY (`applicantId`,`titleId`),
  KEY `titleRelation_title_fk` (`titleId`),
  CONSTRAINT `titleRelation_applicant_fk` FOREIGN KEY (`applicantId`) REFERENCES `applicant` (`id`),
  CONSTRAINT `titleRelation_title_fk` FOREIGN KEY (`titleId`) REFERENCES `title` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `titleRelation`
--

LOCK TABLES `titleRelation` WRITE;
/*!40000 ALTER TABLE `titleRelation` DISABLE KEYS */;
INSERT INTO `titleRelation` VALUES (1,4),(12,4),(15,4),(19,4),(1,5),(12,5),(17,5);
/*!40000 ALTER TABLE `titleRelation` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-04-01 23:29:16
