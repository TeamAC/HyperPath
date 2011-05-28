-- MySQL dump 10.13  Distrib 5.5.12, for Linux (i686)
--
-- Host: localhost    Database: hyperPath
-- ------------------------------------------------------
-- Server version	5.5.12

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `address` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `street` varchar(45) NOT NULL,
  `zip` varchar(45) NOT NULL,
  `city` varchar(45) NOT NULL,
  `state` varchar(45) NOT NULL,
  `country` varchar(45) NOT NULL,
  `ext` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `address`
--

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address` DISABLE KEYS */;
/*!40000 ALTER TABLE `address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ads`
--

DROP TABLE IF EXISTS `ads`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ads` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` text NOT NULL,
  `shortDescription` varchar(45) NOT NULL,
  `startDate` datetime NOT NULL,
  `endDate` datetime NOT NULL,
  `services_id` int(11) NOT NULL,
  `advertisers_id` int(11) NOT NULL,
  PRIMARY KEY (`id`,`services_id`,`advertisers_id`),
  KEY `fk_ads_services1` (`services_id`),
  KEY `fk_ads_advertisers1` (`advertisers_id`),
  CONSTRAINT `fk_ads_services1` FOREIGN KEY (`services_id`) REFERENCES `services` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_ads_advertisers1` FOREIGN KEY (`advertisers_id`) REFERENCES `advertisers` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ads`
--

LOCK TABLES `ads` WRITE;
/*!40000 ALTER TABLE `ads` DISABLE KEYS */;
/*!40000 ALTER TABLE `ads` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `advertisers`
--

DROP TABLE IF EXISTS `advertisers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `advertisers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` varchar(45) DEFAULT NULL COMMENT '	',
  `entities_id` int(11) NOT NULL,
  PRIMARY KEY (`id`,`entities_id`),
  KEY `fk_advertisers_entities1` (`entities_id`),
  CONSTRAINT `fk_advertisers_entities1` FOREIGN KEY (`entities_id`) REFERENCES `entities` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `advertisers`
--

LOCK TABLES `advertisers` WRITE;
/*!40000 ALTER TABLE `advertisers` DISABLE KEYS */;
/*!40000 ALTER TABLE `advertisers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `categories` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `label` varchar(45) NOT NULL,
  `description` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clients`
--

DROP TABLE IF EXISTS `clients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clients` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `lastName` varchar(45) NOT NULL,
  `login` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `gender` enum('Male','Female') NOT NULL,
  `birthdate` date NOT NULL,
  `entities_id` int(11) NOT NULL,
  PRIMARY KEY (`id`,`entities_id`),
  KEY `fk_clients_entities1` (`entities_id`),
  CONSTRAINT `fk_clients_entities1` FOREIGN KEY (`entities_id`) REFERENCES `entities` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clients`
--

LOCK TABLES `clients` WRITE;
/*!40000 ALTER TABLE `clients` DISABLE KEYS */;
/*!40000 ALTER TABLE `clients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clients_bookmarked_services`
--

DROP TABLE IF EXISTS `clients_bookmarked_services`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clients_bookmarked_services` (
  `clients_id` int(11) NOT NULL,
  `services_id` int(11) NOT NULL,
  PRIMARY KEY (`clients_id`,`services_id`),
  KEY `fk_clients_has_services_services1` (`services_id`),
  KEY `fk_clients_has_services_clients1` (`clients_id`),
  CONSTRAINT `fk_clients_has_services_clients1` FOREIGN KEY (`clients_id`) REFERENCES `clients` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_clients_has_services_services1` FOREIGN KEY (`services_id`) REFERENCES `services` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clients_bookmarked_services`
--

LOCK TABLES `clients_bookmarked_services` WRITE;
/*!40000 ALTER TABLE `clients_bookmarked_services` DISABLE KEYS */;
/*!40000 ALTER TABLE `clients_bookmarked_services` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clients_reviews`
--

DROP TABLE IF EXISTS `clients_reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clients_reviews` (
  `clients_id` int(11) NOT NULL,
  `reviews_id` int(11) NOT NULL,
  PRIMARY KEY (`clients_id`,`reviews_id`),
  KEY `fk_clients_has_reviews_reviews1` (`reviews_id`),
  KEY `fk_clients_has_reviews_clients1` (`clients_id`),
  CONSTRAINT `fk_clients_has_reviews_clients1` FOREIGN KEY (`clients_id`) REFERENCES `clients` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_clients_has_reviews_reviews1` FOREIGN KEY (`reviews_id`) REFERENCES `reviews` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clients_reviews`
--

LOCK TABLES `clients_reviews` WRITE;
/*!40000 ALTER TABLE `clients_reviews` DISABLE KEYS */;
/*!40000 ALTER TABLE `clients_reviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `emails`
--

DROP TABLE IF EXISTS `emails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `emails` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `address_UNIQUE` (`address`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `emails`
--

LOCK TABLES `emails` WRITE;
/*!40000 ALTER TABLE `emails` DISABLE KEYS */;
/*!40000 ALTER TABLE `emails` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entities`
--

DROP TABLE IF EXISTS `entities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entities` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entities`
--

LOCK TABLES `entities` WRITE;
/*!40000 ALTER TABLE `entities` DISABLE KEYS */;
/*!40000 ALTER TABLE `entities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entities_has_address`
--

DROP TABLE IF EXISTS `entities_has_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entities_has_address` (
  `entities_id` int(11) NOT NULL,
  `address_id` int(11) NOT NULL,
  PRIMARY KEY (`entities_id`,`address_id`),
  KEY `fk_entities_has_address_address1` (`address_id`),
  KEY `fk_entities_has_address_entities1` (`entities_id`),
  CONSTRAINT `fk_entities_has_address_entities1` FOREIGN KEY (`entities_id`) REFERENCES `entities` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_entities_has_address_address1` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entities_has_address`
--

LOCK TABLES `entities_has_address` WRITE;
/*!40000 ALTER TABLE `entities_has_address` DISABLE KEYS */;
/*!40000 ALTER TABLE `entities_has_address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entities_has_emails`
--

DROP TABLE IF EXISTS `entities_has_emails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entities_has_emails` (
  `entities_id` int(11) NOT NULL,
  `emails_id` int(11) NOT NULL,
  PRIMARY KEY (`entities_id`,`emails_id`),
  KEY `fk_entities_has_emails_emails1` (`emails_id`),
  KEY `fk_entities_has_emails_entities1` (`entities_id`),
  CONSTRAINT `fk_entities_has_emails_entities1` FOREIGN KEY (`entities_id`) REFERENCES `entities` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_entities_has_emails_emails1` FOREIGN KEY (`emails_id`) REFERENCES `emails` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entities_has_emails`
--

LOCK TABLES `entities_has_emails` WRITE;
/*!40000 ALTER TABLE `entities_has_emails` DISABLE KEYS */;
/*!40000 ALTER TABLE `entities_has_emails` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entities_has_faxes`
--

DROP TABLE IF EXISTS `entities_has_faxes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entities_has_faxes` (
  `entities_id` int(11) NOT NULL,
  `faxes_id` int(11) NOT NULL,
  PRIMARY KEY (`entities_id`,`faxes_id`),
  KEY `fk_entities_has_faxes_faxes1` (`faxes_id`),
  KEY `fk_entities_has_faxes_entities1` (`entities_id`),
  CONSTRAINT `fk_entities_has_faxes_entities1` FOREIGN KEY (`entities_id`) REFERENCES `entities` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_entities_has_faxes_faxes1` FOREIGN KEY (`faxes_id`) REFERENCES `faxes` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entities_has_faxes`
--

LOCK TABLES `entities_has_faxes` WRITE;
/*!40000 ALTER TABLE `entities_has_faxes` DISABLE KEYS */;
/*!40000 ALTER TABLE `entities_has_faxes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entities_has_phones`
--

DROP TABLE IF EXISTS `entities_has_phones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entities_has_phones` (
  `entities_id` int(11) NOT NULL,
  `phones_id` int(11) NOT NULL,
  PRIMARY KEY (`entities_id`,`phones_id`),
  KEY `fk_entities_has_phones_phones1` (`phones_id`),
  KEY `fk_entities_has_phones_entities1` (`entities_id`),
  CONSTRAINT `fk_entities_has_phones_entities1` FOREIGN KEY (`entities_id`) REFERENCES `entities` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_entities_has_phones_phones1` FOREIGN KEY (`phones_id`) REFERENCES `phones` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entities_has_phones`
--

LOCK TABLES `entities_has_phones` WRITE;
/*!40000 ALTER TABLE `entities_has_phones` DISABLE KEYS */;
/*!40000 ALTER TABLE `entities_has_phones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `faxes`
--

DROP TABLE IF EXISTS `faxes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `faxes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `number` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `number_UNIQUE` (`number`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `faxes`
--

LOCK TABLES `faxes` WRITE;
/*!40000 ALTER TABLE `faxes` DISABLE KEYS */;
/*!40000 ALTER TABLE `faxes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `openingHours`
--

DROP TABLE IF EXISTS `openingHours`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `openingHours` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `openTime` time NOT NULL,
  `closeTime` time NOT NULL,
  `days` int(11) NOT NULL DEFAULT '127',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `openingHours`
--

LOCK TABLES `openingHours` WRITE;
/*!40000 ALTER TABLE `openingHours` DISABLE KEYS */;
/*!40000 ALTER TABLE `openingHours` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `phones`
--

DROP TABLE IF EXISTS `phones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `phones` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `number` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `number_UNIQUE` (`number`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phones`
--

LOCK TABLES `phones` WRITE;
/*!40000 ALTER TABLE `phones` DISABLE KEYS */;
/*!40000 ALTER TABLE `phones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reviews`
--

DROP TABLE IF EXISTS `reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reviews` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rating` int(11) NOT NULL,
  `description` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reviews`
--

LOCK TABLES `reviews` WRITE;
/*!40000 ALTER TABLE `reviews` DISABLE KEYS */;
/*!40000 ALTER TABLE `reviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `services`
--

DROP TABLE IF EXISTS `services`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `services` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `label` varchar(45) NOT NULL,
  `description` varchar(45) NOT NULL,
  `categories_id` int(11) NOT NULL,
  `location` varchar(45) NOT NULL,
  `usersReview` varchar(45) NOT NULL,
  `entities_id` int(11) NOT NULL,
  `openingHours_id` int(11) NOT NULL,
  `rating` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`,`categories_id`,`entities_id`,`openingHours_id`),
  KEY `fk_services_categories` (`categories_id`),
  KEY `fk_services_entities1` (`entities_id`),
  KEY `fk_services_openingHours1` (`openingHours_id`),
  CONSTRAINT `fk_services_categories` FOREIGN KEY (`categories_id`) REFERENCES `categories` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_services_entities1` FOREIGN KEY (`entities_id`) REFERENCES `entities` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_services_openingHours1` FOREIGN KEY (`openingHours_id`) REFERENCES `openingHours` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `services`
--

LOCK TABLES `services` WRITE;
/*!40000 ALTER TABLE `services` DISABLE KEYS */;
/*!40000 ALTER TABLE `services` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `services_has_reviews`
--

DROP TABLE IF EXISTS `services_has_reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `services_has_reviews` (
  `services_id` int(11) NOT NULL,
  `reviews_id` int(11) NOT NULL,
  PRIMARY KEY (`services_id`,`reviews_id`),
  KEY `fk_services_has_reviews_reviews1` (`reviews_id`),
  KEY `fk_services_has_reviews_services1` (`services_id`),
  CONSTRAINT `fk_services_has_reviews_services1` FOREIGN KEY (`services_id`) REFERENCES `services` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_services_has_reviews_reviews1` FOREIGN KEY (`reviews_id`) REFERENCES `reviews` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `services_has_reviews`
--

LOCK TABLES `services_has_reviews` WRITE;
/*!40000 ALTER TABLE `services_has_reviews` DISABLE KEYS */;
/*!40000 ALTER TABLE `services_has_reviews` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2011-05-28 23:00:26
