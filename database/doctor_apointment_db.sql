/*M!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19-12.2.2-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: doctor_apointment_db
-- ------------------------------------------------------
-- Server version	12.2.2-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*M!100616 SET @OLD_NOTE_VERBOSITY=@@NOTE_VERBOSITY, NOTE_VERBOSITY=0 */;

--
-- Table structure for table `disponibilite`
--

DROP TABLE IF EXISTS `disponibilite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `disponibilite` (
  `id_dispo` int(11) NOT NULL AUTO_INCREMENT,
  `dispo_id_med` varchar(15) DEFAULT NULL,
  `debut_dispo` time NOT NULL,
  `fin_dispo` time NOT NULL,
  `date_dispo` date NOT NULL,
  PRIMARY KEY (`id_dispo`),
  KEY `disponibilite_medecin_FK` (`dispo_id_med`),
  CONSTRAINT `disponibilite_medecin_FK` FOREIGN KEY (`dispo_id_med`) REFERENCES `medecin` (`id_med`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `disponibilite`
--

SET @OLD_AUTOCOMMIT=@@AUTOCOMMIT, @@AUTOCOMMIT=0;
LOCK TABLES `disponibilite` WRITE;
/*!40000 ALTER TABLE `disponibilite` DISABLE KEYS */;
INSERT INTO `disponibilite` VALUES
(2,'M004','13:00:00','15:00:00','2026-05-02'),
(4,'M004','11:32:00','13:32:00','2026-05-01');
/*!40000 ALTER TABLE `disponibilite` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;
SET AUTOCOMMIT=@OLD_AUTOCOMMIT;

--
-- Table structure for table `medecin`
--

DROP TABLE IF EXISTS `medecin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `medecin` (
  `id_med` varchar(15) NOT NULL,
  `nom_med` varchar(50) NOT NULL,
  `prenom_med` varchar(50) DEFAULT NULL,
  `specialite` varchar(50) NOT NULL,
  `lieu` varchar(50) NOT NULL,
  `mdp_med` varchar(255) NOT NULL,
  `taux_horaire` decimal(20,2) NOT NULL,
  PRIMARY KEY (`id_med`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medecin`
--

SET @OLD_AUTOCOMMIT=@@AUTOCOMMIT, @@AUTOCOMMIT=0;
LOCK TABLES `medecin` WRITE;
/*!40000 ALTER TABLE `medecin` DISABLE KEYS */;
INSERT INTO `medecin` VALUES
('M001','FETRA','','oreille','fianarantsoa','$2a$10$X6eEPWRK6pLLTtgyH6QhwuR8.mqE8y6PZKJxRQNzpRrzxpShUjAnG',0.00),
('M004','ROGER','Bary','Dentiste','Tanambao, Fianarantsoa','$2a$10$Yg5WR/UAQCL5hXqz.dK3xeT852Vpxi5FhQgRPHuGXplTd.8WX5j6K',10000.00),
('M005','RAKOTO','','Cardiologue','Toliara','$2a$10$DpI.K/f188NFTdWtsQ.YeuzDBtcKC54T5rEiNGsTOYJvBh5.8/1/q',25000.00);
/*!40000 ALTER TABLE `medecin` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;
SET AUTOCOMMIT=@OLD_AUTOCOMMIT;

--
-- Table structure for table `patient`
--

DROP TABLE IF EXISTS `patient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `patient` (
  `id_pat` varchar(15) NOT NULL,
  `nom_pat` varchar(50) NOT NULL,
  `prenom_pat` varchar(50) DEFAULT NULL,
  `date_nais` date NOT NULL,
  `mdp_pat` varchar(255) NOT NULL,
  `email_pat` varchar(100) NOT NULL,
  PRIMARY KEY (`id_pat`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient`
--

SET @OLD_AUTOCOMMIT=@@AUTOCOMMIT, @@AUTOCOMMIT=0;
LOCK TABLES `patient` WRITE;
/*!40000 ALTER TABLE `patient` DISABLE KEYS */;
INSERT INTO `patient` VALUES
('P006','MARCHEL','Jacque','2004-01-01','$2a$10$dQcJ8CtoT1NVsi19ogos6Oz5lguugMkU9DqG46Lo8ekjK2Iw00GqK','nantenainarld@gmail.com'),
('P008','RALANDISON','Nantenaina','2002-02-01','$2a$10$ACfEv33/WCKRH6gxh1CxrewPrtnCPK2hGJ1szrux2ylLzeJL3R4rq','test@sasa.com'),
('P009','NOM','','2004-02-05','$2a$10$d2FbBPrPAUJmovYfKv6ssObPMHSpKbHJk18qENoYLqq0KtWQzbyK6','nantenaina@gmail.com'),
('P010','JACK','Maxim','2008-04-16','$2a$10$Yhf988beCoIVReW/0bAkk.RtKHANqAwkqEKH6M0CK68sq/lrQ.Q2S','maxim@gmail.com'),
('P011','LOREM','Ipsum','2002-12-03','$2a$10$.Ba1jdiXpvp387qqWfLOKOidcu6cM2sWHNEyjOlNxSfoxfbB6KkMW','lorem@gmail.com');
/*!40000 ALTER TABLE `patient` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;
SET AUTOCOMMIT=@OLD_AUTOCOMMIT;

--
-- Table structure for table `rdv`
--

DROP TABLE IF EXISTS `rdv`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `rdv` (
  `id_rdv` int(11) NOT NULL AUTO_INCREMENT,
  `rdv_id_med` varchar(15) DEFAULT NULL,
  `rdv_id_pat` varchar(15) DEFAULT NULL,
  `date_rdv` date NOT NULL,
  `etat_rdv` enum('en attente','confirmé','annulé') NOT NULL DEFAULT 'en attente',
  `heure_debut` time NOT NULL,
  `heure_fin` time NOT NULL,
  `date_pris_rdv` datetime NOT NULL,
  PRIMARY KEY (`id_rdv`),
  KEY `rdv_medecin_FK` (`rdv_id_med`),
  KEY `rdv_patient_FK` (`rdv_id_pat`),
  CONSTRAINT `rdv_medecin_FK` FOREIGN KEY (`rdv_id_med`) REFERENCES `medecin` (`id_med`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `rdv_patient_FK` FOREIGN KEY (`rdv_id_pat`) REFERENCES `patient` (`id_pat`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rdv`
--

SET @OLD_AUTOCOMMIT=@@AUTOCOMMIT, @@AUTOCOMMIT=0;
LOCK TABLES `rdv` WRITE;
/*!40000 ALTER TABLE `rdv` DISABLE KEYS */;
INSERT INTO `rdv` VALUES
(4,'M005','P006','2026-05-02','confirmé','13:40:00','14:20:00','2026-04-25 21:59:32'),
(6,'M004','P008','2026-05-02','confirmé','13:40:00','14:40:00','2026-04-29 19:01:44');
/*!40000 ALTER TABLE `rdv` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;
SET AUTOCOMMIT=@OLD_AUTOCOMMIT;

--
-- Dumping routines for database 'doctor_apointment_db'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*M!100616 SET NOTE_VERBOSITY=@OLD_NOTE_VERBOSITY */;

-- Dump completed on 2026-05-01 19:49:22
