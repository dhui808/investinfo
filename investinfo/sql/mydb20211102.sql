-- MySQL dump 10.13  Distrib 8.0.23, for Win64 (x86_64)
--
-- Host: localhost    Database: financial_products
-- ------------------------------------------------------
-- Server version	8.0.23

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
-- Table structure for table `investing_com_history`
--

DROP TABLE IF EXISTS `investing_com_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `investing_com_history` (
  `release_week_tuesday` char(8) NOT NULL,
  `instrument` varchar(20) NOT NULL,
  `week_starting` char(8) NOT NULL,
  `close_price` float(11,4) NOT NULL,
  PRIMARY KEY (`instrument`,`release_week_tuesday`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
/*!50500 PARTITION BY LIST  COLUMNS(instrument)
(PARTITION OIL VALUES IN ('OIL') ENGINE = InnoDB,
 PARTITION NG VALUES IN ('NG') ENGINE = InnoDB,
 PARTITION GOLD VALUES IN ('GOLD') ENGINE = InnoDB,
 PARTITION USD_INDEX VALUES IN ('USD_INDEX') ENGINE = InnoDB,
 PARTITION USD_CAD VALUES IN ('USD_CAD') ENGINE = InnoDB,
 PARTITION EURO_FX VALUES IN ('EURO_FX') ENGINE = InnoDB,
 PARTITION US10Y VALUES IN ('US10Y') ENGINE = InnoDB,
 PARTITION SPX500 VALUES IN ('SPX500') ENGINE = InnoDB,
 PARTITION NASDAQ VALUES IN ('NASDAQ') ENGINE = InnoDB,
 PARTITION DOW30 VALUES IN ('DOW30') ENGINE = InnoDB) */;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `investing_com_history`
--
-- WHERE:  release_week_tuesday='20211102'

LOCK TABLES `investing_com_history` WRITE;
/*!40000 ALTER TABLE `investing_com_history` DISABLE KEYS */;
INSERT INTO `investing_com_history` VALUES ('20211102','OIL','20211031',81.1700),('20211102','NG','20211031',5.6220),('20211102','GOLD','20211031',1819.9500),('20211102','USD_INDEX','20211031',94.2070),('20211102','USD_CAD','20211031',1.2457),('20211102','EURO_FX','20211031',1.1568),('20211102','US10Y','20211031',1.4550),('20211102','SPX500','20211031',4697.5298),('20211102','NASDAQ','20211031',15971.5996),('20211102','DOW30','20211031',36327.9492);
/*!40000 ALTER TABLE `investing_com_history` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-11-08  9:19:59
