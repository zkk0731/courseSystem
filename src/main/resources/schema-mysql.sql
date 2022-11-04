CREATE TABLE IF NOT EXISTS `course` (
  `id` varchar(20) NOT NULL,
  `name` varchar(20) DEFAULT NULL,
  `day` int DEFAULT NULL,
  `start` int DEFAULT NULL,
  `end` int DEFAULT NULL,
  `credit` int DEFAULT NULL,
  PRIMARY KEY (`id`);
  
  CREATE TABLE IF NOT EXISTS `student` (
  `id` varchar(20) NOT NULL,
  `name` varchar(20) NOT NULL,
  `courseid` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`);