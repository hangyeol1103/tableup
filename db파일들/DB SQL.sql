drop database tableup;
CREATE DATABASE IF NOT EXISTS tableup;
USE tableup;

CREATE TABLE `User` (
   us_num INT AUTO_INCREMENT PRIMARY KEY, 
   us_id VARCHAR(20) NOT NULL UNIQUE,
   us_pw VARCHAR(255) NOT NULL,
   us_name VARCHAR(50) NOT NULL,         
   us_phone VARCHAR(20) NOT NULL UNIQUE,
   us_email VARCHAR(50) NOT NULL UNIQUE,
   us_nickname VARCHAR(30) NOT NULL,
   us_sociallogin BOOLEAN DEFAULT FALSE,
   us_created DATETIME DEFAULT CURRENT_TIMESTAMP,
   us_state INT NOT NULL DEFAULT 0
);

CREATE TABLE `Admin` (
   ad_num INT AUTO_INCREMENT PRIMARY KEY, 
   ad_id VARCHAR(20) NOT NULL UNIQUE,
   ad_pw VARCHAR(255) NOT NULL
);

CREATE TABLE Region (
   reg_num	INT AUTO_INCREMENT PRIMARY KEY,
   reg_main	VARCHAR(50) NOT NULL
);

CREATE TABLE DetailRegion (
   dreg_num	INT AUTO_INCREMENT PRIMARY KEY,
   dreg_sub	VARCHAR(50) NOT	NULL,
   dreg_reg_num	INT	NULL,
   CONSTRAINT FK_DetailRegion_Region
    FOREIGN KEY (dreg_reg_num) REFERENCES Region(reg_num)
);