-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: lms_db
-- ------------------------------------------------------
-- Server version	5.5.5-10.4.32-MariaDB

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
-- Table structure for table `books`
--

DROP TABLE IF EXISTS `books`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `books` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `number_of_copies_available` int(11) DEFAULT NULL,
  `published_year` int(11) DEFAULT NULL,
  `isbn` varchar(32) DEFAULT NULL,
  `cover_url` varchar(512) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `genre` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `books`
--

LOCK TABLES `books` WRITE;
/*!40000 ALTER TABLE `books` DISABLE KEYS */;
INSERT INTO `books` VALUES (1,4,1960,'978-0061120084','http://books.google.com/books/content?id=3t5dtAEACAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api','Harper Lee','Tiểu thuyết','To Kill a Mockingbird'),(2,7,1949,'978-0451524935',NULL,'George Orwell','Phản địa đàng','1984'),(3,4,1925,'978-0743273565',NULL,'F. Scott Fitzgerald','Tiểu thuyết','The Great Gatsby'),(4,6,1954,'978-0618640157',NULL,'J.R.R. Tolkien','Giả tưởng','The Lord of the Rings'),(5,8,1813,'978-1503290563',NULL,'Jane Austen','Lãng mạn','Pride and Prejudice'),(6,3,1951,'978-0316769488',NULL,'J.D. Salinger','Tiểu thuyết','The Catcher in the Rye'),(7,4,1851,'978-1503280786',NULL,'Herman Melville','Phiêu lưu','Moby Dick'),(8,1,1869,'978-1420952138',NULL,'Leo Tolstoy','Tiểu thuyết lịch sử','War and Peace'),(9,15,1988,'978-0061122416',NULL,'Paulo Coelho','Giả tưởng','The Alchemist'),(10,10,2011,'978-0062316097',NULL,'Yuval Noah Harari','Lịch sử','Sapiens: A Brief History of Humankind'),(11,6,2018,'978-0399590504',NULL,'Tara Westover','Hồi ký','Educated'),(12,8,2018,'978-1524763138',NULL,'Michelle Obama','Hồi ký','Becoming'),(13,5,1947,'978-0553296983',NULL,'Anne Frank','Tiểu sử','The Diary of a Young Girl'),(14,7,2003,'978-0767908184',NULL,'Bill Bryson','Khoa học','A Short History of Nearly Everything'),(15,4,1962,'978-0345386236',NULL,'Barbara W. Tuchman','Lịch sử','The Guns of August'),(16,6,1965,'978-0441013593',NULL,'Frank Herbert','Khoa học viễn tưởng','Dune'),(17,9,1979,'978-0345391803',NULL,'Douglas Adams','Khoa học viễn tưởng','The Hitchhiker\'s Guide to the Galaxy'),(18,12,1997,'978-0590353427',NULL,'J.K. Rowling','Giả tưởng','Harry Potter and the Sorcerer\'s Stone'),(19,5,1996,'978-0553593716',NULL,'George R.R. Martin','Giả tưởng','A Game of Thrones'),(20,6,1953,'978-1451673319',NULL,'Ray Bradbury','Phản địa đàng','Fahrenheit 451'),(21,5,1932,'978-0060850524',NULL,'Aldous Huxley','Phản địa đàng','Brave New World'),(22,8,2007,'978-0756404741',NULL,'Patrick Rothfuss','Giả tưởng','The Name of the Wind'),(23,10,1989,'978-1982137274',NULL,'Stephen R. Covey','Phát triển bản thân','The 7 Habits of Highly Effective People'),(24,11,1936,'978-0671027032',NULL,'Dale Carnegie','Phát triển bản thân','How to Win Friends and Influence People'),(25,8,1997,'978-1612680194',NULL,'Robert T. Kiyosaki','Tài chính','Rich Dad Poor Dad'),(26,5,2011,'978-0374533557',NULL,'Daniel Kahneman','Tâm lý học','Thinking, Fast and Slow'),(27,15,2018,'978-0735211292',NULL,'James Clear','Phát triển bản thân','Atomic Habits'),(28,9,2011,'978-0307887894',NULL,'Eric Ries','Kinh doanh','The Lean Startup'),(29,7,2008,'978-0132350884',NULL,'Robert C. Martin','Công nghệ phần mềm','Clean Code'),(30,6,1999,'978-0201616224',NULL,'Andrew Hunt','Công nghệ phần mềm','The Pragmatic Programmer'),(31,4,1994,'978-0201633610',NULL,'Erich Gamma','Công nghệ phần mềm','Design Patterns'),(32,3,1990,'978-0262033848',NULL,'Thomas H. Cormen','Khoa học máy tính','Introduction to Algorithms'),(33,4,1995,'978-0136042594',NULL,'Stuart Russell','Trí tuệ nhân tạo','Artificial Intelligence: A Modern Approach'),(34,10,2015,'978-0984782857',NULL,'Gayle Laakmann McDowell','Khoa học máy tính','Cracking the Coding Interview'),(35,7,2005,'978-0307473479',NULL,'Stieg Larsson','Trinh thám','The Girl with the Dragon Tattoo'),(36,8,2012,'978-0307588371',NULL,'Gillian Flynn','Trinh thám','Gone Girl'),(37,10,2003,'978-0307474278',NULL,'Dan Brown','Bí ẩn','The Da Vinci Code'),(38,9,1939,'978-0312330873',NULL,'Agatha Christie','Bí ẩn','And Then There Were None'),(39,6,180,'978-0140449334',NULL,'Marcus Aurelius','Triết học','Meditations'),(40,3,1883,'978-0140441185',NULL,'Friedrich Nietzsche','Triết học','Thus Spoke Zarathustra'),(41,5,1991,'978-0374530716','http://books.google.com/books/content?id=J8nE3B5lD9AC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api','Jostein Gaarder','Triết học','Sophie\'s World'),(42,7,1936,'978-6049692408',NULL,'Vũ Trọng Phụng','Trào phúng','Số Đỏ'),(43,15,1941,'978-6042058315',NULL,'Tô Hoài','Thiếu nhi','Dế Mèn Phiêu Lưu Ký'),(44,6,1937,'978-6049533367',NULL,'Ngô Tất Tố','Hiện thực xã hội','Tắt Đèn'),(45,20,2008,'978-6042188708',NULL,'Nguyễn Nhật Ánh','Thiếu nhi','Cho Tôi Xin Một Vé Đi Tuổi Thơ'),(46,10,1957,'978-6042083232',NULL,'Đoàn Giỏi','Phiêu lưu','Đất Rừng Phương Nam');
/*!40000 ALTER TABLE `books` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `borrow`
--

DROP TABLE IF EXISTS `borrow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `borrow` (
  `borrow_id` int(11) NOT NULL AUTO_INCREMENT,
  `book_id` int(11) DEFAULT NULL,
  `due_date` datetime(6) DEFAULT NULL,
  `issue_date` datetime(6) DEFAULT NULL,
  `return_date` datetime(6) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`borrow_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `borrow`
--

LOCK TABLES `borrow` WRITE;
/*!40000 ALTER TABLE `borrow` DISABLE KEYS */;
/*!40000 ALTER TABLE `borrow` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `loans`
--

DROP TABLE IF EXISTS `loans`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `loans` (
  `book_id` int(11) NOT NULL,
  `due_date` date NOT NULL,
  `fine_amount` decimal(10,2) DEFAULT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `loan_date` date NOT NULL,
  `member_id` int(11) NOT NULL,
  `return_date` date DEFAULT NULL,
  `fine_status` varchar(20) DEFAULT NULL,
  `status` enum('ACTIVE','OVERDUE','RETURNED') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `loans`
--

LOCK TABLES `loans` WRITE;
/*!40000 ALTER TABLE `loans` DISABLE KEYS */;
INSERT INTO `loans` VALUES (8,'2025-10-27',NULL,1,'2025-10-20',2,NULL,'NO_FINE','ACTIVE'),(1,'2025-11-03',NULL,2,'2025-10-20',2,NULL,'NO_FINE','ACTIVE');
/*!40000 ALTER TABLE `loans` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservations`
--

DROP TABLE IF EXISTS `reservations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservations` (
  `book_id` int(11) NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `member_id` int(11) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `expire_at` datetime(6) DEFAULT NULL,
  `status` enum('ACTIVE','CANCELLED','EXPIRED','FULFILLED') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKnpvra05wpaxym9vi2o8mm5stp` (`book_id`,`member_id`,`status`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservations`
--

LOCK TABLES `reservations` WRITE;
/*!40000 ALTER TABLE `reservations` DISABLE KEYS */;
INSERT INTO `reservations` VALUES (1,1,2,'2025-10-20 12:38:21.000000','2025-10-23 12:38:21.000000','ACTIVE');
/*!40000 ALTER TABLE `reservations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reviews`
--

DROP TABLE IF EXISTS `reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reviews` (
  `approved` bit(1) NOT NULL,
  `book_id` int(11) NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rating` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `comment` text DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK8dwwvmbh89prx5sbwddb860tc` (`book_id`,`user_id`),
  KEY `FKcgy7qjc1r99dp117y9en6lxye` (`user_id`),
  CONSTRAINT `FK6a9k6xvev80se5rreqvuqr7f9` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`),
  CONSTRAINT `FKcgy7qjc1r99dp117y9en6lxye` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reviews`
--

LOCK TABLES `reviews` WRITE;
/*!40000 ALTER TABLE `reviews` DISABLE KEYS */;
INSERT INTO `reviews` VALUES (_binary '\0',1,1,5,2,'2025-10-19 17:34:07.000000','Hay lắm nhe');
/*!40000 ALTER TABLE `reviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(50) NOT NULL,
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `uk_role_name` (`role_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'ROLE_ADMIN'),(2,'ROLE_USER');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_role` (
  `role_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FKa68196081fvovjhkek5m97n3y` (`role_id`),
  CONSTRAINT `FKa68196081fvovjhkek5m97n3y` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`),
  CONSTRAINT `FKj345gk1bovqvfame88rcx7yyx` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (1,1),(2,2);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `password` varchar(200) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uk_users_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin','admin@lms.com','Admin User','$2a$10$q./P7iK1SCjvLI2V78iGwupTv0pFwyrydjtsvTfrmR9PiVwSmxSU.'),(2,'user','user@lms.com','Normal User','$2a$10$mUR.OluEX6PhnEqcZErt4Oe.5whFeyew0yq2.BdAUg2elGHZelv0y');
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

-- Dump completed on 2025-10-20 21:06:51
