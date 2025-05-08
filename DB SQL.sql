drop database tableup;
CREATE DATABASE IF NOT EXISTS tableup;
USE tableup;

CREATE TABLE User (
  us_no INT AUTO_INCREMENT PRIMARY KEY, 
  us_id VARCHAR(20) NOT NULL,
  us_pw VARCHAR(255) NOT NULL,
  us_name VARCHAR(50) NOT NULL,         
  us_phone VARCHAR(20),                 
  us_email VARCHAR(50) NOT NULL UNIQUE,
  us_nickname VARCHAR(30) NOT NULL,
  us_sociallogin BOOLEAN DEFAULT FALSE,
  us_created DATETIME DEFAULT CURRENT_TIMESTAMP,
  us_authority ENUM('user', 'admin') NOT NULL DEFAULT 'user');

CREATE TABLE TagType (
  tt_num INT AUTO_INCREMENT PRIMARY KEY,
  tt_name VARCHAR(50) NOT NULL
);

CREATE TABLE Tag (
  tag_num INT AUTO_INCREMENT PRIMARY KEY,       -- 태그 번호 (PK)
  tag_name VARCHAR(50) NOT NULL,                -- 태그명
  tag_tt_num INT NOT NULL,                      -- 태그분류 번호 (FK)
  FOREIGN KEY (tag_tt_num) REFERENCES TagType(tt_num)  -- 분류 테이블과 연결
);
