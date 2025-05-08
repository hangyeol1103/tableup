CREATE DATABASE IF NOT EXISTS tableup;
USE tableup;

CREATE TABLE User (
    us_id INT AUTO_INCREMENT PRIMARY KEY,             -- 사용자 아이디 (PK)
    us_name VARCHAR(50) NOT NULL,                     -- 사용자 실명
    us_phone VARCHAR(20),                             -- 사용자 전화번호
    us_email VARCHAR(100) NOT NULL UNIQUE,            -- 사용자 이메일
    us_nickname VARCHAR(50) NOT NULL,                 -- 사용자 닉네임
    us_sociallogin BOOLEAN DEFAULT FALSE,             -- 소셜 로그인 여부
    us_created DATETIME DEFAULT CURRENT_TIMESTAMP     -- 가입 일자
);
